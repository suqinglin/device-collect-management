package com.vians.admin.mapper;

import com.vians.admin.model.RoleInfo;
import com.vians.admin.model.UserDetailInfo;
import com.vians.admin.model.UserInfo;
import com.vians.admin.request.query.UserQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ViansUserMapper {

    UserInfo getUserByPhone(String phone);

    UserInfo getUserById(long id);

    UserInfo getUserByName(@Param("name") String name);

    List<UserDetailInfo> getUserList(UserQuery userQuery);

    void addUser(UserDetailInfo userInfo);

    List<RoleInfo> getRoles();
}
