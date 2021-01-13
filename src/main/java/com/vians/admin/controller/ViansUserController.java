package com.vians.admin.controller;

import com.vians.admin.model.RoleInfo;
import com.vians.admin.model.UserDetailInfo;
import com.vians.admin.model.UserInfo;
import com.vians.admin.request.RxId;
import com.vians.admin.request.RxLogin;
import com.vians.admin.request.RxUser;
import com.vians.admin.request.query.UserQuery;
import com.vians.admin.response.Page;
import com.vians.admin.response.ResponseCode;
import com.vians.admin.response.ResponseData;
import com.vians.admin.security.UserDetailsImpl;
import com.vians.admin.service.RedisService;
import com.vians.admin.service.UserService;
import com.vians.admin.service.ViansUserService;
import com.vians.admin.utils.JwtTokenUtil;
import com.vians.admin.web.AppBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

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

    @PostMapping("/login")
    public ResponseData login(@Valid @RequestBody RxLogin login) {
        String userPhone = login.getUserPhone();
        String password = login.getPassword();
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userPhone, password);
        Authentication authentication = null;
        try {
            authentication = authenticationManager.authenticate(authenticationToken);
        } catch (AuthenticationException e) {
//            e.printStackTrace();
            ResponseCode responseCode = ResponseCode.ERROR_USERNAME_PWD_INCORRECT;
            logger.info("AuthenticationException {}", e.getClass());
            if (e instanceof BadCredentialsException) {
                responseCode = ResponseCode.ERROR_USERNAME_PWD_INCORRECT;
            }
            if (e instanceof AccountExpiredException) {
                responseCode = ResponseCode.ERROR_USERNAME_PWD_INCORRECT;
            }
            if (e instanceof DisabledException) {
                responseCode = ResponseCode.ERROR_ACCOUNT_INACTIVE;
            }
            if (e instanceof LockedException) {
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

    @PostMapping("/info")
    public ResponseData info() {
        Long userId = appBean.getCurrentUserId();
        ResponseData responseData = new ResponseData(ResponseCode.SUCCESS);
        responseData.addData("userId", userId);
        return responseData;
    }

    @PostMapping("/list")
    public ResponseData getUserList(@RequestBody UserQuery userQuery) {
        Page<UserDetailInfo> roomInfoPage = userService.getUserList(userQuery);
        ResponseData responseData = new ResponseData(ResponseCode.SUCCESS);
        responseData.addData("list", roomInfoPage.getList());
        responseData.addData("total", roomInfoPage.getTotal());
        return responseData;
    }

    @PostMapping("/add")
    public ResponseData addUser(@RequestBody RxUser user) {

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
        userInfo.setProjectId(user.getProjectId());
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
}
