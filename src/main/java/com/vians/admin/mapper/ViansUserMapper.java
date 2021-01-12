package com.vians.admin.mapper;

import com.vians.admin.model.UserInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ViansUserMapper {

    UserInfo getUserByPhone(String phone);

    UserInfo getUserById(long id);
}
