package com.vians.admin.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vians.admin.response.ResponseCode;
import com.vians.admin.response.ResponseData;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 当访问接口没有权限时，自定义的返回结果
 * Created by wangkun23 on 2019/1/14.
 */
@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {
    @Resource
    ObjectMapper objectMapper;

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException ex) throws IOException, ServletException {
        ResponseData responseData = ResponseData.error(ResponseCode.ERROR_ACCOUNT_AUTH, ex.getMessage());
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType("application/json");
        response.getWriter().println(objectMapper.writeValueAsString(responseData));
        response.getWriter().flush();
    }
}
