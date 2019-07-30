package com.suql.devicecollect.service;

import com.suql.devicecollect.model.UserInfo;

public interface UserService {

    UserInfo getUserByAccount(String account);

    UserInfo getUserById(long id);

    boolean validPassword(Long userId, String password);

    void registerUser(String userPhone, String password, String nickName);
}
