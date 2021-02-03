package com.vians.admin.mapper;

import com.vians.admin.model.AuthorizeInfo;
import com.vians.admin.request.RxAuthorize;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @ClassName AuthorizeMapper
 * @Description TODO
 * @Author su qinglin
 * @Date 2021/1/18 11:01
 * @Version 1.0
 **/
@Mapper
public interface AuthorizeMapper {

    List<AuthorizeInfo> getAuthorizeList(@Param("userId") long userId, @Param("roomId") long roomId);

    void addAuthorize(RxAuthorize rxAuthorize);

    void deleteAuthorize(@Param("id") long id);

    void deleteAuthorizeByRoom(@Param("roomId") long roomId, @Param("type") int type);
}
