package com.vians.admin.mapper;

import com.vians.admin.model.RootUserInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @ClassName RootUserMapper
 * @Description TODO
 * @Author su qinglin
 * @Date 2021/1/28 21:07
 * @Version 1.0
 **/
@Mapper
public interface RootUserMapper {

    void updateRootUser(RootUserInfo rootUserInfo);

    RootUserInfo getRootUserById(@Param("userId") long userId);
}
