package com.suql.devicecollect.mapper;

import com.suql.devicecollect.model.UserInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {

    UserInfo getUserByAccount(String account);

    UserInfo getUserById(long id);

    String getMaxId();

    void saveUserInfo(UserInfo userInfo);
}
