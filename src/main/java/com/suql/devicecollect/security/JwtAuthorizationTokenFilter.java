package com.suql.devicecollect.security;

import com.suql.devicecollect.model.UserInfo;
import com.suql.devicecollect.service.RedisService;
import com.suql.devicecollect.service.UserService;
import com.suql.devicecollect.utils.JwtTokenUtil;
import io.jsonwebtoken.Claims;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

public class JwtAuthorizationTokenFilter extends OncePerRequestFilter {

    @Resource
    public RedisService redisService;

    @Resource
    public UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        String token = httpServletRequest.getHeader("token");
        SecurityContextHolder.getContext().setAuthentication(null);
        if (!StringUtils.isEmpty(token) && SecurityContextHolder.getContext().getAuthentication() == null) {
            boolean valid = JwtTokenUtil.verifyJwtToken(token, redisService);
            Claims claims = JwtTokenUtil.parseClaimsJws(token);
            String userId = null;
            if (claims != null) {
                userId = claims.getSubject();
            }
            if (valid && !StringUtils.isEmpty(userId)) {
                UserInfo userInfo = userService.getUserById(Long.valueOf(userId));
                Collection<? extends GrantedAuthority> authorities = Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));
                UserDetails userDetails =  new UserDetailsImpl(
                        userInfo.getId(),
                        userInfo.getAccount(),
                        userInfo.getPassword(),
                        authorities,
                        true);
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
