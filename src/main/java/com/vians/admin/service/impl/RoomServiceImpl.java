package com.vians.admin.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.vians.admin.mapper.RoomMapper;
import com.vians.admin.model.RoomInfo;
import com.vians.admin.model.RoomModelInfo;
import com.vians.admin.response.Page;
import com.vians.admin.response.Pageable;
import com.vians.admin.service.RoomService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @ClassName RoomServiceImpl
 * @Description TODO
 * @Author su qinglin
 * @Date 2021/1/8 11:42
 * @Version 1.0
 **/
@Service
public class RoomServiceImpl implements RoomService {

    @Resource
    private RoomMapper roomMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addRoom(RoomInfo roomInfo) {
        roomInfo.setCreateTime(new Date());
        roomMapper.addRoom(roomInfo);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Page<RoomInfo> getRoomList(String roomName, long floorId, Pageable pageable) {
        PageHelper.startPage(pageable.getPageNumber(), pageable.getPageSize());
        List<RoomInfo> roomInfoList = roomMapper.getRoomList(roomName, floorId);
        PageInfo<RoomInfo> pageInfo = new PageInfo<>(roomInfoList);
        return new Page<>(roomInfoList, pageInfo.getTotal(), pageable);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteRoom(long id) {
        roomMapper.deleteRoom(id);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void editRoom(RoomInfo roomInfo) {
        roomInfo.setUpdateTime(new Date());
        roomMapper.editRoom(roomInfo);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<RoomModelInfo> getRoomModels() {
        return roomMapper.getRoomModels();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public RoomInfo getRoomByNameInFloor(String roomName, long floorId) {
        return roomMapper.getRoomByNameInFloor(roomName, floorId);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public RoomInfo getRoomById(long id) {
        return roomMapper.getRoomById(id);
    }
}
