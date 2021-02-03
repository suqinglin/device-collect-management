package com.vians.admin.service.impl;

import com.vians.admin.mapper.ViansUserMapper;
import com.vians.admin.model.UserInfo;
import com.vians.admin.service.ViansUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
public class ViansUserServiceImpl implements ViansUserService {

    final Logger logger = LoggerFactory.getLogger(getClass());
    @Resource
    private ViansUserMapper userMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public UserInfo getUserByPhone(String phone) {
        return userMapper.getUserByPhone(phone);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public UserInfo getUserById(long id) {
        return userMapper.getUserById(id);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public UserInfo loginByPhone(String phone) {
        return userMapper.getUserByPhone(phone);
    }
}
