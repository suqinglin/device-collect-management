package com.suql.devicecollect.mapper;

import com.suql.devicecollect.model.UserInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {

    UserInfo getUserByAccount(String account);

    UserInfo getUserById(long id);

    String getMaxId();

    void saveUserInfo(UserInfo userInfo);

    List<UserInfo> getUserList();

    void editUser(UserInfo userInfo);

    void modifyPwd(UserInfo userInfo);

    void deleteUserById(int id);
}
