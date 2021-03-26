package com.vians.admin.service;

import com.vians.admin.model.AuthorizeContentInfo;
import com.vians.admin.model.AuthorizeInfo;
import com.vians.admin.request.RxAuthorize;

import java.util.List;

/**
 * @ClassName AuthorizeService
 * @Description TODO
 * @Author su qinglin
 * @Date 2021/1/18 11:00
 * @Version 1.0
 **/
public interface AuthorizeService {

    List<AuthorizeInfo> getAuthorizeList(long userId, long roomId);

    long addAuthorize(RxAuthorize rxAuthorize);

    void addAuthorizeRoom(long authorizeId, long roomId, int position);

    void deleteAuthorize(long id);

    void deleteAuthorizeTempContent(long authorizeId);

    void deleteAuthorizeByRoom(long roomId, int type);

    void deleteAuthorizeTempContentByRoom(long roomId, int type);

    int findEmptyPosition(long roomId, int type);

    long addAuthorizeContent(AuthorizeContentInfo authorizeContentInfo);

    void  deleteAuthorizeByUser(long userId);
}
