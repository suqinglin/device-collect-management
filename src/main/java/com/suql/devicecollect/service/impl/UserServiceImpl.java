package com.suql.devicecollect.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.suql.devicecollect.mapper.UserMapper;
import com.suql.devicecollect.model.UserInfo;
import com.suql.devicecollect.response.Page;
import com.suql.devicecollect.response.Pageable;
import com.suql.devicecollect.service.UserService;
import com.suql.devicecollect.utils.DoNetUnicodeUtils;
import com.suql.devicecollect.utils.Md5Util;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

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

    @Override
    public Page<UserInfo> getUserList(Pageable pageable) {
        PageHelper.startPage(pageable.getPageNumber(), pageable.getPageSize());
        List<UserInfo> userInfoList = userMapper.getUserList();
        PageInfo<UserInfo> pageInfo = new PageInfo<>(userInfoList);
        return new Page<>(userInfoList, pageInfo.getTotal(), pageable);
    }

    @Override
    public void modifyPwd(int id, String newPwd) {
        UserInfo userInfo = new UserInfo();
        userInfo.setId(id);
        userInfo.setPassword(Md5Util.toMd5(newPwd));
        userMapper.modifyPwd(userInfo);
    }

    @Override
    public void editUser(int id, String account, String nickName) {
        UserInfo userInfo = new UserInfo();
        userInfo.setId(id);
        if (!StringUtils.isEmpty(account)) {
            userInfo.setAccount(account);
        }
        if (!StringUtils.isEmpty(nickName)) {
            userInfo.setNickName(nickName);
        }
        userInfo.setCreateTime(System.currentTimeMillis());
        userMapper.editUser(userInfo);
    }

    @Override
    public void deleteUsers(List<Integer> userIds) {
        for (int userId: userIds) {
            userMapper.deleteUserById(userId);
        }
    }
}
