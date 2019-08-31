package com.suql.devicecollect.controller;

import com.suql.devicecollect.model.UserInfo;
import com.suql.devicecollect.request.*;
import com.suql.devicecollect.response.Page;
import com.suql.devicecollect.response.Pageable;
import com.suql.devicecollect.response.ResponseCode;
import com.suql.devicecollect.response.ResponseData;
import com.suql.devicecollect.security.UserDetailsImpl;
import com.suql.devicecollect.service.RedisService;
import com.suql.devicecollect.service.UserService;
import com.suql.devicecollect.utils.JwtTokenUtil;
import com.suql.devicecollect.utils.Md5Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

@CrossOrigin
@RestController
@RequestMapping("/user")
public class UserController {

    final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private AuthenticationManager authenticationManager;

    @Resource
    public RedisService redisService;

    @Resource
    public UserService userService;

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

    @PostMapping("/list")
    public ResponseData list(@RequestBody RxUserList rxUserList) {
        Pageable pageable = new Pageable(rxUserList.getPageIndex(), rxUserList.getPageSize());
        Page<UserInfo> userInfoPage = userService.getUserList(pageable);
        ResponseData responseData = new ResponseData(ResponseCode.SUCCESS);
        responseData.addData("userList", userInfoPage.getList());
        responseData.addData("count", userInfoPage.getTotal());
        return responseData;
    }

    @PostMapping("/register")
    public ResponseData register(@RequestBody RxRegisterUser rxRegisterUser) {
        try {
            UserInfo userInfo = userService.getUserByAccount(rxRegisterUser.getUserPhone());
            if (userInfo != null) {
                return new ResponseData(ResponseCode.ERROR_ACCOUNT_EXIST);
            }
            userService.registerUser(rxRegisterUser.getUserPhone(),
                    rxRegisterUser.getPassword(),
                    rxRegisterUser.getNickName());
            return new ResponseData(ResponseCode.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseData(ResponseCode.ERROR);
        }
    }

    @PostMapping("/modifyPwd")
    public ResponseData modifyPwd(@RequestBody RxModifyUserPwd rxModifyUserPwd) {

        UserInfo userInfo = userService.getUserById(rxModifyUserPwd.getUserId());
        String oldPwdMd5 = Md5Util.toMd5(rxModifyUserPwd.getOldPwd());
        if (userInfo.getPassword().equals(oldPwdMd5)) {
            userService.modifyPwd(rxModifyUserPwd.getUserId(), rxModifyUserPwd.getNewPwd());
            return new ResponseData(ResponseCode.SUCCESS);
        } else {
            return new ResponseData(ResponseCode.ERROR_PWD_NOTAGREE);
        }
    }

    @PostMapping("/edit")
    public ResponseData editUser(@RequestBody RxEditUser rxEditUser) {
        UserInfo userInfo = userService.getUserById(rxEditUser.getUserId());
        if (userInfo == null) {
            return new ResponseData(ResponseCode.ERROR_ACCOUNT_NOT_EXIST);
        }
        userService.editUser(rxEditUser.getUserId(), rxEditUser.getAccount(), rxEditUser.getNickName());
        return new ResponseData(ResponseCode.SUCCESS);
    }

    @PostMapping("/deleteUsers")
    public ResponseData deleteUsers(@RequestBody RxDeleteUsers rxDeleteUsers) {
        try {
            userService.deleteUsers(rxDeleteUsers.getUserIds());
            return new ResponseData(ResponseCode.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseData(ResponseCode.ERROR);
        }
    }
}
