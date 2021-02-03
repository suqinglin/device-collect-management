package com.vians.admin.service;

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

    void addAuthorize(RxAuthorize rxAuthorize);

    void deleteAuthorize(long id);

    void deleteAuthorizeByRoom(long roomId, int type);
}
