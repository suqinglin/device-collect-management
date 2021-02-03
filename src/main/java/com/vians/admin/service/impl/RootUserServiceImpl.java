package com.vians.admin.service.impl;

import com.vians.admin.mapper.RootUserMapper;
import com.vians.admin.model.RootUserInfo;
import com.vians.admin.service.RootUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @ClassName RootUserServiceImpl
 * @Description TODO
 * @Author su qinglin
 * @Date 2021/1/28 21:05
 * @Version 1.0
 **/
@Service
public class RootUserServiceImpl implements RootUserService {

    @Resource
    RootUserMapper rootUserMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateRootUser(RootUserInfo rootUserInfo) {
        rootUserInfo.setUpdateTime(new Date());
        rootUserMapper.updateRootUser(rootUserInfo);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public RootUserInfo getRootUserById(long userId) {
        return rootUserMapper.getRootUserById(userId);
    }
}
