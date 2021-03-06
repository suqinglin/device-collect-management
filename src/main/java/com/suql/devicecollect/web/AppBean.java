package com.suql.devicecollect.web;

import com.suql.devicecollect.security.UserDetailsImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AppBean {

    public UserDetailsImpl getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() instanceof UserDetailsImpl) {
            return (UserDetailsImpl) authentication.getPrincipal();
        }
        return null;
    }

    /**
     * 获取当前用户id
     *
     * @return
     */
    public Long getCurrentUserId() {
        UserDetailsImpl userDetails = getCurrentUser();
        if (userDetails == null) {
            return null;
        }
        return userDetails.getId();
    }
}
