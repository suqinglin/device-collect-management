package com.suql.devicecollect.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.suql.devicecollect.response.ResponseCode;
import com.suql.devicecollect.response.ResponseData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 当未登录或者token失效访问接口时，自定义的返回结果
 * Created by wangkun23 on 2019/1/14.
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    final Logger logger = LoggerFactory.getLogger(getClass());

    @Resource
    ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {

        ResponseCode responseCode = ResponseCode.ERROR_USERNAME_PWD_INCORRECT;
        if (authException instanceof InsufficientAuthenticationException) {
            responseCode = ResponseCode.ERROR_ACCOUNT_AUTH;
        }
        ResponseData responseData = ResponseData.error(responseCode, authException.getMessage());
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.getWriter().println(objectMapper.writeValueAsString(responseData));
        response.getWriter().flush();
    }
}
