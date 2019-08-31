package com.suql.devicecollect.service;

import com.suql.devicecollect.model.UserInfo;
import com.suql.devicecollect.response.Page;
import com.suql.devicecollect.response.Pageable;

import java.util.List;

public interface UserService {

    UserInfo getUserByAccount(String account);

    UserInfo getUserById(long id);

    boolean validPassword(Long userId, String password);

    void registerUser(String userPhone, String password, String nickName);

    void modifyPwd(int id, String newPwd);

    Page<UserInfo> getUserList(Pageable pageable);

    void editUser(int id, String account, String nickName);

    void deleteUsers(List<Integer> userIds);
}
