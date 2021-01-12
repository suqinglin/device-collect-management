package com.vians.admin.security;

//import com.suql.devicecollect.model.UserInfo;
//import com.suql.devicecollect.service.UserService;

import com.vians.admin.model.UserInfo;
import com.vians.admin.service.ViansUserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Collection;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Resource
    public ViansUserService userService;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Collection<? extends GrantedAuthority> authorities = Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));
        UserInfo userInfo = userService.getUserByPhone(s);
        return new UserDetailsImpl(
                userInfo.getId(),
                userInfo.getPhone(),
                userInfo.getPassword(),
                authorities,
                true);
    }
}
