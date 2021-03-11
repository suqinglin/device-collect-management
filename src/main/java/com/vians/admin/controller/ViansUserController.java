package com.vians.admin.controller;

import com.alibaba.fastjson.JSONObject;
import com.vians.admin.common.CommConstants;
import com.vians.admin.excel.ExcelPOIHelper;
import com.vians.admin.excel.ExcelUser;
import com.vians.admin.model.RoleInfo;
import com.vians.admin.model.RootUserInfo;
import com.vians.admin.model.UserDetailInfo;
import com.vians.admin.model.UserInfo;
import com.vians.admin.request.*;
import com.vians.admin.request.query.UserQuery;
import com.vians.admin.response.Page;
import com.vians.admin.response.ResponseCode;
import com.vians.admin.response.ResponseData;
import com.vians.admin.security.UserDetailsImpl;
import com.vians.admin.service.*;
import com.vians.admin.utils.HttpUtil;
import com.vians.admin.utils.JwtTokenUtil;
import com.vians.admin.utils.KeyUtil;
import com.vians.admin.web.AppBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@CrossOrigin
@RestController
@RequestMapping("/vians/user")
public class ViansUserController {

    final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private AuthenticationManager authenticationManager;

    @Resource
    public RedisService redisService;

    @Autowired
    private AppBean appBean;

    @Autowired
    private UserService userService;

    @Resource
    private ViansUserService viansUserService;

    @Resource
    private RootUserService rootUserService;

    @Resource
    private RoomService roomService;

    @Resource
    private DeviceService deviceService;

    @PostMapping("/login")
    public ResponseData login(@Valid @RequestBody RxLogin login) {
        String userPhone = login.getUserPhone();
        String password = login.getPassword();
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userPhone, password);
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(authenticationToken);
        } catch (AuthenticationException e) {
            ResponseCode responseCode = ResponseCode.ERROR_USERNAME_PWD_INCORRECT;
            logger.info("AuthenticationException {}", e.getClass());
            if (e instanceof BadCredentialsException) {
                // 账号存在，但密码错误
                logger.info("=============BadCredentialsException");
                responseCode = ResponseCode.ERROR_USERNAME_PWD_INCORRECT;
            }
            if (e instanceof AccountExpiredException) {
                // 账号过期（可能是超过了token的有效截至时间）
                logger.info("=============AccountExpiredException");
                responseCode = ResponseCode.ERROR_USERNAME_PWD_INCORRECT;
            }
            if (e instanceof DisabledException) {
                // 账户待激活
                responseCode = ResponseCode.ERROR_ACCOUNT_INACTIVE;
            }
            if (e instanceof LockedException) {
                // 账户已锁定
                responseCode = ResponseCode.ERROR_ACCOUNT_LOCK;
            }
            return ResponseData.error(responseCode);
        }
        // 产生Token，并保存到redis
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String userToken = JwtTokenUtil.createToken(userDetails.getId(), redisService);

        // 成功应答，返回token
        ResponseData responseData = new ResponseData(ResponseCode.SUCCESS);
        responseData.addData("userToken", userToken);
        return responseData;
    }

    @PostMapping("/saveRootUser")
    public ResponseData saveRootUser(@RequestBody RxSaveRootUser rxSaveRootUser) {
        String tokenStr;
        // 获取Token
        Map<String, Object> requestParam = new HashMap<>();
        requestParam.put("UserName", rxSaveRootUser.getRootUserPhone());
        JSONObject getTokenRsp = HttpUtil.getInstance().doRequest(requestParam, CommConstants.C_API_URL + "/api/GetToken");
        if (getTokenRsp != null) {
            int tokenResult = (int) getTokenRsp.get("Result");
            if (tokenResult == 0) {
                tokenStr = getTokenRsp.get("Token").toString();
                 // 登录
                String keyL = KeyUtil.getKeyL(tokenStr, rxSaveRootUser.getPassword());
                logger.info("keyL: {}", keyL);
                requestParam.put("KeyL", keyL);
                JSONObject loginRsp = HttpUtil.getInstance().doRequest(requestParam, CommConstants.C_API_URL + "/api/Login");
                if (loginRsp != null) {
                    int loginResult = (int) loginRsp.get("Result");
                    if (loginResult == 0) {
                        long userId = Long.parseLong(loginRsp.get("UserID").toString());
                        String cnt = loginRsp.get("CNT").toString();
                        // 保存到数据库
                        // 删除数据库中跟即将添加的用户同手机号的数据
                        userService.deleteUserByPhone(rxSaveRootUser.getRootUserPhone());
                        userService.deleteProjectManager(rxSaveRootUser.getProjectId());
                        userService.updateUsersRootId(rxSaveRootUser.getProjectId(), userId);
                        UserDetailInfo userDetailInfo = new UserDetailInfo();
                        userDetailInfo.setPhone(rxSaveRootUser.getRootUserPhone());
                        userDetailInfo.setRoleId(4); // 角色ID为4：项目管理员
                        userDetailInfo.setProjectId(rxSaveRootUser.getProjectId());
                        userDetailInfo.setPassword(rxSaveRootUser.getPassword());
                        userDetailInfo.setRootId(userId);
                        // 添加项目管理员到用户表
                        userService.addUser(userDetailInfo);
                        RootUserInfo rootUserInfo = new RootUserInfo();
                        rootUserInfo.setUserId(userId);
                        rootUserInfo.setCnt(Long.parseLong(cnt, 16));
                        rootUserInfo.setToken(tokenStr);
                        rootUserInfo.setPhone(rxSaveRootUser.getRootUserPhone());
                        rootUserInfo.setPassword(rxSaveRootUser.getPassword());
                        rootUserService.updateRootUser(rootUserInfo);
                    } else if (loginResult == 1003) {
                        return ResponseData.error(ResponseCode.ERROR_DEVICE_ROOT_USER_PASSWORD_ERROR);
                    } else {
                        return HttpUtil.getInstance().doResult(loginResult);
                    }
                }
            } else if (tokenResult == 1002) {
                return ResponseData.error(ResponseCode.ERROR_DEVICE_ROOT_USER_PHONE_ERROR);
            } else {
                return HttpUtil.getInstance().doResult(tokenResult);
            }
        }
        return ResponseData.success();
    }

    @PostMapping("/info")
    public ResponseData info(@RequestBody RxGetInfo getInfo) {
        Long userId = appBean.getCurrentUserId();
        ResponseData responseData = new ResponseData(ResponseCode.SUCCESS);
        if (userId == null) {
            return new ResponseData(ResponseCode.ERROR_UN_AUTHORIZE_LOGIN);
        }
        UserInfo userInfo = userService.getUserInfo(userId);
        responseData.addData("projectId", appBean.getProjectId());
        responseData.addData("name", userInfo.getPhone());
        responseData.addData("roles", userInfo.getPermissions());
        responseData.addData("avatar", "/static/images/m0.jpg");
        responseData.addData("introduction", userInfo.getUserName());
        String collectDevMac = deviceService.getDeviceMacByBrowser(getInfo.getBrowserUUID()); // 指纹读卡器设备
        responseData.addData("collectDevMac", StringUtils.isEmpty(collectDevMac)? "" : collectDevMac);
        return responseData;
    }

    @PostMapping("/list")
    public ResponseData getUserList(@RequestBody UserQuery userQuery) {
        Long projectId = appBean.getProjectId();
        if (projectId == null) {
            return ResponseData.error(ResponseCode.ERROR_USER_IS_ILLEGAL);
        }
        userQuery.setProjectId(projectId);
        Page<UserDetailInfo> roomInfoPage = userService.getUserList(userQuery);
        ResponseData responseData = new ResponseData(ResponseCode.SUCCESS);
        responseData.addData("list", roomInfoPage.getList());
        responseData.addData("total", roomInfoPage.getTotal());
        return responseData;
    }

    /**
     * 获取与指定房间未关联的人员
     * @param userQuery
     * @return
     */
    @PostMapping("/unbindList")
    public ResponseData getUnbindUserList(@RequestBody UserQuery userQuery) {
        Long projectId = appBean.getProjectId();
        if (projectId == null) {
            return ResponseData.error(ResponseCode.ERROR_USER_IS_ILLEGAL);
        }
        userQuery.setProjectId(projectId);
        Page<UserDetailInfo> roomInfoPage = userService.getUnbindUserList(userQuery);
        ResponseData responseData = new ResponseData(ResponseCode.SUCCESS);
        responseData.addData("list", roomInfoPage.getList());
        responseData.addData("total", roomInfoPage.getTotal());
        return responseData;
    }

    /**
     * 获取与指定房间已关联的人员
     * @param userQuery
     * @return
     */
    @PostMapping("/bindList")
    public ResponseData getBindUserList(@RequestBody UserQuery userQuery) {
        ResponseData responseData = new ResponseData(ResponseCode.SUCCESS);
        responseData.addData("list", userService.getBindUserList(userQuery));
        return responseData;
    }

    @PostMapping("/add")
    public ResponseData addUser(@RequestBody RxUser user) {
        Long projectId = appBean.getProjectId();
        if (projectId == null) {
            return ResponseData.error(ResponseCode.ERROR_USER_IS_ILLEGAL);
        }
        if (viansUserService.getUserByPhone(user.getPhone()) != null) {
            return new ResponseData(ResponseCode.ERROR_USER_PHONE_EXIST);
        }
        UserDetailInfo userInfo = new UserDetailInfo();
        userInfo.setUserName(user.getUserName());
        userInfo.setPassword(user.getPassword());
        userInfo.setGender(user.getGender());
        userInfo.setCardId(user.getCardId());
        userInfo.setPhone(user.getPhone());
        userInfo.setRoleId(user.getRoleId());
        userInfo.setOrganization(user.getOrganization());
        userInfo.setDepartment(user.getDepartment());
        userInfo.setWorkNumber(user.getWorkNumber());
        userInfo.setDuty(user.getDuty());
        userInfo.setProjectId(projectId);
        userService.addUser(userInfo);
        return new ResponseData(ResponseCode.SUCCESS);
    }

    @PostMapping("/roles")
    public ResponseData getRoles() {
        List<RoleInfo> roles = userService.getRoles();
        ResponseData responseData = new ResponseData(ResponseCode.SUCCESS);
        responseData.addData("roles", roles);
        return responseData;
    }

    @PostMapping("/rolesWithPermissions")
    public ResponseData getRolesWithPermissions() {
        List<RoleInfo> roles = userService.getRolesWithPermissions();
        ResponseData responseData = new ResponseData(ResponseCode.SUCCESS);
        responseData.addData("roles", roles);
        return responseData;
    }

    @PostMapping("/saveRolePermissions")
    public ResponseData saveRolePermissions(@RequestBody RxSaveRolePermissions saveRolePermissions) {
        userService.saveRolePermissions(saveRolePermissions.getRoleId(), saveRolePermissions.getPermissionIds());
        return ResponseData.success();
    }

    @PostMapping("/delete")
    public ResponseData deleteUser(@RequestBody RxId delete) {
        userService.deleteUser(delete.getId());
        return new ResponseData(ResponseCode.SUCCESS);
    }

    @PostMapping("/edit")
    public ResponseData editUser(@RequestBody RxUser user) {

        // 数据库已经存在一条与当前数据不同，但手机号码相同的数据
        if (!StringUtils.isEmpty(user.getPhone())) {
            UserInfo ui = viansUserService.getUserByPhone(user.getPhone());
            if (ui != null && ui.getId() != user.getId()) {
                return new ResponseData(ResponseCode.ERROR_USER_PHONE_EXIST);
            }
        }
        UserDetailInfo userInfo = new UserDetailInfo();
        userInfo.setId(user.getId());
        userInfo.setUserName(user.getUserName());
        userInfo.setPassword(user.getPassword());
        userInfo.setGender(user.getGender());
        userInfo.setCardId(user.getCardId());
        userInfo.setPhone(user.getPhone());
        userInfo.setRoleId(user.getRoleId());
        userInfo.setOrganization(user.getOrganization());
        userInfo.setDepartment(user.getDepartment());
        userInfo.setWorkNumber(user.getWorkNumber());
        userInfo.setDuty(user.getDuty());
        userInfo.setProjectId(user.getProjectId());
        userService.editUser(userInfo);
        return new ResponseData(ResponseCode.SUCCESS);
    }

    /**
     * 将人员绑定到房间
     * @param rxBindUser
     * @return
     */
    @PostMapping("/bind")
    public ResponseData bindUser(@RequestBody RxBindUser rxBindUser) {
        Long userId = appBean.getCurrentUserId();
        if (userId == null) {
            return new ResponseData(ResponseCode.ERROR_UN_AUTHORIZE_LOGIN);
        }
        userService.bindUser(rxBindUser.getRoomId(), rxBindUser.getUserId(), userId);
        roomService.updateRoomState(rxBindUser.getRoomId(), CommConstants.ROOM_STATE_CHECK_IN);
        return new ResponseData(ResponseCode.SUCCESS);
    }

    /**
     * 解除绑定关系
     * @param rxId
     * @return
     */
    @PostMapping("/unbind")
    public ResponseData unbindUser(@RequestBody RxId rxId) {
        userService.unbindUser(rxId.getId());
        return new ResponseData(ResponseCode.SUCCESS);
    }

    @PostMapping("/uploadUsersExcel")
    public ResponseData uploadUsersExcel(@RequestParam(value = "file") MultipartFile file,
                                         @RequestParam(value = "projectId") Long projectId) {
        logger.info("Filename {}", file.getOriginalFilename());
        logger.info("projectId {}", projectId);
        String fileName = file.getOriginalFilename();
        if (fileName == null || (!fileName.endsWith(".xls") && !fileName.endsWith(".xlsx"))) {
            return ResponseData.error();
        }
        try {
            List<ExcelUser> userList = new ExcelPOIHelper().readUsersExcel(
                    Objects.requireNonNull(file.getOriginalFilename()),
                    file.getInputStream());
            logger.info("userList = {}", userList.toString());
            userService.batchAddUsers(projectId, userList);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseData.success();
    }

    @PostMapping("/resetPsw")
    public ResponseData resetPsw(@RequestBody RxId rxId) {
        userService.modifyPsw(rxId.getId(), "123456");
        return ResponseData.success();
    }

    @PostMapping("/modifyPsw")
    public ResponseData modifyPsw(@RequestBody RxModifyPsw modifyPsw) {
        Long userId = appBean.getCurrentUserId();
        if (userId == null) {
            return new ResponseData(ResponseCode.ERROR_ACCOUNT_NOT_LOGIN);
        }
        if (!userService.checkOldPsw(userId, modifyPsw.getOldPassword())) {
            return new ResponseData(ResponseCode.ERROR_ACCOUNT_OLD_PASSWORD_ERROR);
        }
        userService.modifyPsw(userId, modifyPsw.getNewPassword());
        return ResponseData.success();
    }
}
