package com.vians.admin.mapper;

import com.vians.admin.model.AuthorizeContentInfo;
import com.vians.admin.model.AuthorizeInfo;
import com.vians.admin.model.AuthorizeRoomInfo;
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

    void updateAuthorize(RxAuthorize rxAuthorize);

    void deleteAuthorize(@Param("id") long id);

    void deleteAuthorizeByRoom(@Param("roomId") long roomId, @Param("type") int type);

    int findEmptyPosition(@Param("roomId") long roomId, @Param("type") int type);

    void addAuthorizeRoom(AuthorizeRoomInfo authorizeRoomInfo);

    void addAuthorizeContent(AuthorizeContentInfo authorizeContentInfo);

    /**
     * 根据授权值（如卡号、密码）查询授权信息
     * @param value
     * @param roomId
     * @return
     */
    AuthorizeInfo findAuthorizeByValue(@Param("value") String value, @Param("roomId") long roomId);

    void deleteAuthorizeTempContent(@Param("authorizeId") long authorizeId);

    void deleteAuthorizeTempContentByRoom(@Param("roomId") long roomId, @Param("type") int type);

    void deleteAuthorizeByUser(@Param("userId") long userId);

    void updateAuthorizeRoom(AuthorizeRoomInfo authorizeRoomInfo);
}
