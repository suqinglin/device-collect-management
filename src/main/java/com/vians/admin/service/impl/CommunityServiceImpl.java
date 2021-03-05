package com.vians.admin.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.vians.admin.mapper.*;
import com.vians.admin.model.*;
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
    private BuildingMapper buildingMapper;

    @Resource
    private CommunityMapper communityMapper;

    @Resource
    private UnitMapper unitMapper;

    @Resource
    private FloorMapper floorMapper;

    @Resource
    private RoomMapper roomMapper;

    @Resource
    private DeviceMapper deviceMapper;

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
        List<BuildingInfo> buildings = buildingMapper.getBuildingsByCommunityId(id);
        buildings.forEach(buildingInfo -> {
            List<UnitInfo> units = unitMapper.getUnitsByBuildingId(buildingInfo.getId());
            units.forEach(unitInfo -> {
                List<FloorInfo> floors = floorMapper.getFloorsByUnitId(unitInfo.getId());
                floors.forEach(floorInfo -> {
                    List<RoomInfo> rooms = roomMapper.getRoomsByFloorId(floorInfo.getId());
                    rooms.forEach(roomInfo -> {
                        // 删除房间人员关联
                        roomMapper.deleteRoomUserByRoomId(roomInfo.getId());
                        // 删除房间设备关联
                        deviceMapper.unbindDevicesByRoomId(roomInfo.getId());
                    });
                    // 删除楼层下所有房间
                    roomMapper.deleteRoomsByFloorId(floorInfo.getId());
                });
                // 删除单元下所有楼层
                floorMapper.deleteFloorsByUnitId(unitInfo.getId());
            });
            // 删除楼栋下所有单元
            unitMapper.deleteUnitsByBuildingId(buildingInfo.getId());
        });
        // 删除小区下所有楼栋
        buildingMapper.deleteBuildingsByCommunityId(id);
        // 最后删除小区
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
