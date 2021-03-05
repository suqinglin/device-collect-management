package com.vians.admin.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.vians.admin.mapper.DeviceMapper;
import com.vians.admin.mapper.FloorMapper;
import com.vians.admin.mapper.RoomMapper;
import com.vians.admin.model.FloorInfo;
import com.vians.admin.model.RoomInfo;
import com.vians.admin.response.Page;
import com.vians.admin.response.Pageable;
import com.vians.admin.service.FloorService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @ClassName FloorServiceImpl
 * @Description TODO
 * @Author su qinglin
 * @Date 2021/1/8 10:23
 * @Version 1.0
 **/
@Service
public class FloorServiceImpl implements FloorService {

    @Resource
    private FloorMapper floorMapper;

    @Resource
    private RoomMapper roomMapper;

    @Resource
    private DeviceMapper deviceMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addFloor(FloorInfo floorInfo) {
        floorInfo.setCreateTime(new Date());
        floorMapper.addFloor(floorInfo);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Page<FloorInfo> getFloorList(String floorName, long unitId, Long projectId, Pageable pageable) {
        PageHelper.startPage(pageable.getPageNumber(), pageable.getPageSize());
        List<FloorInfo> floorInfoList = floorMapper.getFloorList(floorName, unitId, projectId);
        PageInfo<FloorInfo> pageInfo = new PageInfo<>(floorInfoList);
        return new Page<>(floorInfoList, pageInfo.getTotal(), pageable);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteFloor(long id) {
        List<RoomInfo> rooms = roomMapper.getRoomsByFloorId(id);
        rooms.forEach(roomInfo -> {
            // 删除房间人员关联
            roomMapper.deleteRoomUserByRoomId(roomInfo.getId());
            // 删除房间设备关联
            deviceMapper.unbindDevicesByRoomId(roomInfo.getId());
        });
        // 删除楼层下所有房间
        roomMapper.deleteRoomsByFloorId(id);
        floorMapper.deleteFloor(id);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void editFloor(FloorInfo floorInfo) {
        floorInfo.setUpdateTime(new Date());
        floorMapper.editFloor(floorInfo);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public FloorInfo getFloorByNameInUnit(String floorName, long unitId) {
        return floorMapper.getFloorByNameInUnit(floorName, unitId);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<FloorInfo> getFloorsByUnitId(long id) {
        return floorMapper.getFloorsByUnitId(id);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public FloorInfo getFloorById(long id) {
        return floorMapper.getFloorById(id);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int getFloorCountByUnitId(long id) {
        return floorMapper.getFloorCountByUnitId(id);
    }
}
