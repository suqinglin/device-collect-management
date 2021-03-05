package com.vians.admin.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.vians.admin.mapper.*;
import com.vians.admin.model.BuildingInfo;
import com.vians.admin.model.FloorInfo;
import com.vians.admin.model.RoomInfo;
import com.vians.admin.model.UnitInfo;
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
    public void addBuilding(BuildingInfo buildingInfo) {
        buildingInfo.setCreateTime(new Date());
        buildingMapper.addBuilding(buildingInfo);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Page<BuildingInfo> getBuildingList(String buildingName, long communityId, long projectId, Pageable pageable) {
        PageHelper.startPage(pageable.getPageNumber(), pageable.getPageSize());
        List<BuildingInfo> buildingInfoList = buildingMapper.getBuildingList(buildingName, communityId, projectId);
        PageInfo<BuildingInfo> pageInfo = new PageInfo<>(buildingInfoList);
        return new Page<>(buildingInfoList, pageInfo.getTotal(), pageable);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteBuilding(long id) {
        List<UnitInfo> units = unitMapper.getUnitsByBuildingId(id);
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
        unitMapper.deleteUnitsByBuildingId(id);
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

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int getBuildingCountByCommunityId(long id) {
        return buildingMapper.getBuildingCountByCommunityId(id);
    }
}
