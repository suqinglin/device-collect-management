package com.vians.admin.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.vians.admin.mapper.BuildingMapper;
import com.vians.admin.model.BuildingInfo;
import com.vians.admin.response.Page;
import com.vians.admin.response.Pageable;
import com.vians.admin.service.BuildingService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @ClassName BuildingServiceImpl
 * @Description TODO
 * @Author su qinglin
 * @Date 2021/1/8 9:50
 * @Version 1.0
 **/
@Service
public class BuildingServiceImpl implements BuildingService {

    @Resource
    private BuildingMapper buildingMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addBuilding(BuildingInfo buildingInfo) {
        buildingInfo.setCreateTime(new Date());
        buildingMapper.addBuilding(buildingInfo);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Page<BuildingInfo> getBuildingList(String buildingName, long communityId, Pageable pageable) {
        PageHelper.startPage(pageable.getPageNumber(), pageable.getPageSize());
        List<BuildingInfo> buildingInfoList = buildingMapper.getBuildingList(buildingName, communityId);
        PageInfo<BuildingInfo> pageInfo = new PageInfo<>(buildingInfoList);
        return new Page<>(buildingInfoList, pageInfo.getTotal(), pageable);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteBuilding(long id) {
        buildingMapper.deleteBuilding(id);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void editBuilding(BuildingInfo buildingInfo) {
        buildingInfo.setUpdateTime(new Date());
        buildingMapper.editBuilding(buildingInfo);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public BuildingInfo getBuildingByNameInCommunity(String buildingName, long communityId) {
        return buildingMapper.getBuildingByNameInCommunity(buildingName, communityId);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public BuildingInfo getBuildingById(long id) {
        return buildingMapper.getBuildingById(id);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<BuildingInfo> getBuildingsByCommunityId(long id) {
        return buildingMapper.getBuildingsByCommunityId(id);
    }
}
