package com.vians.admin.web;

import com.vians.admin.security.UserDetailsImpl;
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

    /**
     * 获取Root用户id
     *
     * @return
     */
    public Long getRootUserId() {
        UserDetailsImpl userDetails = getCurrentUser();
        if (userDetails == null) {
            return null;
        }
        return userDetails.getRootId();
    }

    /**
     * 获取用户所属的项目ID
     * @return
     */
    public Long getProjectId() {
        UserDetailsImpl userDetails = getCurrentUser();
        if (userDetails == null) {
            return null;
        }
        return userDetails.getProjectId();
    }
}
