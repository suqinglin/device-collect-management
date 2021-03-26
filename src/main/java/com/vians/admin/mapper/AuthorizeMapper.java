package com.vians.admin.mapper;

import com.vians.admin.model.AuthorizeContentInfo;
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

    int findEmptyPosition(@Param("roomId") long roomId, @Param("type") int type);

    void addAuthorizeRoom(@Param("authorizeId") long authorizeId, @Param("roomId") long roomId, @Param("position") int position);

    void addAuthorizeContent(AuthorizeContentInfo authorizeContentInfo);

    void deleteAuthorizeTempContent(@Param("authorizeId") long authorizeId);

    void deleteAuthorizeTempContentByRoom(@Param("roomId") long roomId, @Param("type") int type);

    void deleteAuthorizeByUser(@Param("userId") long userId);
}
