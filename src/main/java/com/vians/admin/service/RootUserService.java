package com.vians.admin.service;

import com.vians.admin.model.RootUserInfo;

/**
 * @ClassName RootUserService
 * @Description TODO
 * @Author su qinglin
 * @Date 2021/1/28 20:59
 * @Version 1.0
 **/
public interface RootUserService {

    void updateRootUser(RootUserInfo rootUserInfo);

    RootUserInfo getRootUserById(long userId);
}
