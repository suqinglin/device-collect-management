package com.suql.devicecollect.service.impl;

import com.suql.devicecollect.mapper.UserMapper;
import com.suql.devicecollect.model.UserInfo;
import com.suql.devicecollect.service.UserService;
import com.suql.devicecollect.utils.DoNetUnicodeUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    @Override
    public UserInfo getUserByAccount(String account) {
        return userMapper.getUserByAccount(account);
    }

    @Override
    public UserInfo getUserById(long id) {
        return userMapper.getUserById(id);
    }

    @Override
    public boolean validPassword(Long userId, String password) {
        UserInfo userInfo = getUserById(userId);
        if (userInfo == null) {
            return false;
        }
        return userInfo.getPassword().equals(password);
    }

    @Override
    public void registerUser(String userPhone, String password, String nickName) {
        long id = 0;
        if (userMapper.getMaxId() != null) {
            id = Long.valueOf(userMapper.getMaxId()) + 1;
        }
        UserInfo userInfo = new UserInfo();
        userInfo.setId(id);
        userInfo.setAccount(userPhone);
        byte[] passPhrase = DoNetUnicodeUtils.toUnicodeMC(password);
        String md5Password = DigestUtils.md5Hex(passPhrase).toUpperCase();
        userInfo.setPassword(md5Password);
        userInfo.setNickName(nickName);
        userInfo.setCreateTime(System.currentTimeMillis());
        userMapper.saveUserInfo(userInfo);
    }
}
