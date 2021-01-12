package com.vians.admin.controller;

import com.vians.admin.request.RxLogin;
import com.vians.admin.response.ResponseCode;
import com.vians.admin.response.ResponseData;
import com.vians.admin.security.UserDetailsImpl;
import com.vians.admin.service.RedisService;
import com.vians.admin.utils.JwtTokenUtil;
import com.vians.admin.web.AppBean;
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
@RequestMapping("/vians/user")
public class ViansUserController {

    final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private AuthenticationManager authenticationManager;

    @Resource
    public RedisService redisService;

    @Autowired
    private AppBean appBean;

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
}