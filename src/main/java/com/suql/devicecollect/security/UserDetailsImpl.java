package com.suql.devicecollect.security;

import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@ToString
public class UserDetailsImpl implements UserDetails {

    private Long id;
    private String userName;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;
    private boolean enabled;

    public UserDetailsImpl(
            Long id,
            String userName,
            String password,
            Collection<? extends GrantedAuthority> authorities,
            boolean enabled) {
        this.id = id;
        this.userName = userName;
        this.password = password;
        this.authorities = authorities;
        this.enabled = enabled;
    }

    public Long getId() {
        return id;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
