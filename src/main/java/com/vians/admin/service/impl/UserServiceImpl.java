package com.vians.admin.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.vians.admin.mapper.ViansUserMapper;
import com.vians.admin.model.RoleInfo;
import com.vians.admin.model.UserDetailInfo;
import com.vians.admin.request.query.UserQuery;
import com.vians.admin.response.Page;
import com.vians.admin.response.Pageable;
import com.vians.admin.service.UserService;
import com.vians.admin.utils.Md5Util;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @ClassName UserDetailServiceImpl
 * @Description TODO
 * @Author su qinglin
 * @Date 2021/1/12 15:27
 * @Version 1.0
 **/
@Service
public class UserServiceImpl implements UserService {

    @Resource
    ViansUserMapper userMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Page<UserDetailInfo> getUserList(UserQuery userQuery) {
        Pageable pageable = new Pageable(userQuery.getPageIndex(), userQuery.getPageSize());
        PageHelper.startPage(pageable.getPageNumber(), pageable.getPageSize());
        List<UserDetailInfo> userList = userMapper.getUserList(userQuery);
        PageInfo<UserDetailInfo> pageInfo = new PageInfo<>(userList);
        return new Page<>(userList, pageInfo.getTotal(), pageable);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addUser(UserDetailInfo userInfo) {
        userInfo.setCreateTime(new Date());
        userInfo.setPassword(Md5Util.toMd5(userInfo.getPassword()));
        userMapper.addUser(userInfo);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<RoleInfo> getRoles() {
        return userMapper.getRoles();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void editUser(UserDetailInfo userDetailInfo) {
        userDetailInfo.setUpdateTime(new Date());
        userMapper.editUser(userDetailInfo);
        System.out.println("================userDetailInfo=" + userDetailInfo);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteUser(long id) {
        userMapper.deleteUser(id);
    }
}
