package com.vians.admin.service;

import com.vians.admin.model.UserInfo;

public interface ViansUserService {

    UserInfo getUserByPhone(String phone);

    UserInfo getUserById(long id);

//    boolean validPassword(Long userId, String password);
//
//    void registerUser(String userPhone, String password, String nickName);
//
//    void modifyPwd(int id, String newPwd);
//
//    Page<UserInfo> getUserList(Pageable pageable);
//
//    void editUser(int id, String account, String nickName);
//
//    void deleteUsers(List<Integer> userIds);
}
