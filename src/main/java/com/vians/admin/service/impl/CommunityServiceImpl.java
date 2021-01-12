package com.vians.admin.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.vians.admin.mapper.CommunityMapper;
import com.vians.admin.model.CommunityInfo;
import com.vians.admin.response.Page;
import com.vians.admin.response.Pageable;
import com.vians.admin.service.CommunityService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @ClassName CommunityServiceImpl
 * @Description TODO
 * @Author su qinglin
 * @Date 2021/1/8 9:10
 * @Version 1.0
 **/
@Service
public class CommunityServiceImpl implements CommunityService {

    @Resource
    private CommunityMapper communityMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addCommunity(CommunityInfo communityInfo) {
        communityInfo.setCreateTime(new Date());
        communityMapper.addCommunity(communityInfo);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Page<CommunityInfo> getCommunityList(String communityName, long projectId, Pageable pageable) {
        PageHelper.startPage(pageable.getPageNumber(), pageable.getPageSize());
        List<CommunityInfo> communityInfoList = communityMapper.getCommunityList(communityName, projectId);
        PageInfo<CommunityInfo> pageInfo = new PageInfo<>(communityInfoList);
        return new Page<>(communityInfoList, pageInfo.getTotal(), pageable);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteCommunity(long id) {
        communityMapper.deleteCommunity(id);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void editCommunity(CommunityInfo communityInfo) {
        communityInfo.setUpdateTime(new Date());
        communityMapper.editCommunity(communityInfo);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public CommunityInfo getCommunityByNameInProject(String communityName, long projectId) {
        return communityMapper.getCommunityByNameInProject(communityName, projectId);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public CommunityInfo getCommunityById(long id) {
        return communityMapper.getCommunityById(id);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<CommunityInfo> getCommunitiesByProjectId(long id) {
        return communityMapper.getCommunitiesByProjectId(id);
    }
}
