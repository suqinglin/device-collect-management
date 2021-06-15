package com.vians.admin.service.impl;

import com.vians.admin.mapper.AuthorizeMapper;
import com.vians.admin.model.AuthorizeContentInfo;
import com.vians.admin.model.AuthorizeInfo;
import com.vians.admin.model.AuthorizeRoomInfo;
import com.vians.admin.request.RxAuthorize;
import com.vians.admin.service.AuthorizeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @ClassName AuhtorizeServiceImpl
 * @Description TODO
 * @Author su qinglin
 * @Date 2021/1/18 11:00
 * @Version 1.0
 **/
@Service
public class AuthorizeServiceImpl implements AuthorizeService {

    @Resource
    private AuthorizeMapper authorizeMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<AuthorizeInfo> getAuthorizeList(long userId, long roomId) {
        return authorizeMapper.getAuthorizeList(userId, roomId);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public long addAuthorize(RxAuthorize rxAuthorize) {
        rxAuthorize.setCreateTime(new Date());
//        AuthorizeInfo authorizeInfo = authorizeMapper.findAuthorizeByValue(rxAuthorize.getContent(), rxAuthorize.getRoomId());
//        if (authorizeInfo != null) {
//            rxAuthorize.setUpdateTime(new Date());
//            authorizeMapper.updateAuthorize(rxAuthorize);
//            return authorizeInfo.getId();
//        } else {
            authorizeMapper.addAuthorize(rxAuthorize);
            return rxAuthorize.getId();
//        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addAuthorizeRoom(long authorizeId, long roomId, int position) {
        AuthorizeRoomInfo authorizeRoomInfo = new AuthorizeRoomInfo();
        authorizeRoomInfo.setAuthorizeId(authorizeId);
        authorizeRoomInfo.setRoomId(roomId);
        authorizeRoomInfo.setPosition(position);
        authorizeRoomInfo.setCreateTime(new Date());
        authorizeMapper.addAuthorizeRoom(authorizeRoomInfo);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteAuthorize(long id) {
        authorizeMapper.deleteAuthorize(id);
    }

    @Override
    public void deleteAuthorizeTempContent(long authorizeId) {
        authorizeMapper.deleteAuthorizeTempContent(authorizeId);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteAuthorizeByRoom(long roomId, int type) {
        authorizeMapper.deleteAuthorizeByRoom(roomId, type);
    }

    @Override
    public void deleteAuthorizeTempContentByRoom(long roomId, int type) {
        authorizeMapper.deleteAuthorizeTempContentByRoom(roomId, type);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int findEmptyPosition(long roomId, int type) {
        return authorizeMapper.findEmptyPosition(roomId, type);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public AuthorizeInfo findAuthorizeByValue(String value, long roomId) {
        return authorizeMapper.findAuthorizeByValue(value, roomId);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public long addAuthorizeContent(AuthorizeContentInfo authorizeContentInfo) {
        authorizeMapper.addAuthorizeContent(authorizeContentInfo);
        return authorizeContentInfo.getId();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteAuthorizeByUser(long userId) {
        authorizeMapper.deleteAuthorizeByUser(userId);
    }

    @Override
    public void updateAuthorizeRoom(Long authorizeId, long roomId, int position, int type) {
        AuthorizeRoomInfo authorizeRoomInfo = new AuthorizeRoomInfo();
        authorizeRoomInfo.setAuthorizeId(authorizeId);
        authorizeRoomInfo.setRoomId(roomId);
        authorizeRoomInfo.setPosition(position);
        authorizeRoomInfo.setUpdateTime(new Date());
        authorizeRoomInfo.setAuthorizeType(type);
        authorizeMapper.updateAuthorizeRoom(authorizeRoomInfo);
    }
}
