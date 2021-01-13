package com.vians.admin.service;

import com.vians.admin.model.RoleInfo;
import com.vians.admin.model.UserDetailInfo;
import com.vians.admin.request.query.UserQuery;
import com.vians.admin.response.Page;

import java.util.List;

/**
 * @ClassName UserService
 * @Description TODO
 * @Author su qinglin
 * @Date 2021/1/12 15:23
 * @Version 1.0
 **/
public interface UserService {

    Page<UserDetailInfo> getUserList(UserQuery userQuery);

    void addUser(UserDetailInfo userInfo);

    List<RoleInfo> getRoles();

    void editUser(UserDetailInfo userDetailInfo);

    void deleteUser(long id);
}
