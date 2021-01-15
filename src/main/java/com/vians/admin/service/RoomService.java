package com.vians.admin.service;

import com.vians.admin.model.RoomInfo;
import com.vians.admin.model.RoomModelInfo;
import com.vians.admin.response.Page;
import com.vians.admin.response.Pageable;

import java.util.List;

/**
 * @ClassName RoomService
 * @Description TODO
 * @Author su qinglin
 * @Date 2021/1/8 11:40
 * @Version 1.0
 **/
public interface RoomService {

    void addRoom(RoomInfo floorInfo);

    Page<RoomInfo> getRoomList(String roomName, long floorId, Pageable pageable);

    void deleteRoom(long id);

    void editRoom(RoomInfo roomInfo);

    List<RoomModelInfo> getRoomModels();

    RoomInfo getRoomByNameInFloor(String roomName, long floorId);

    RoomInfo getRoomById(long id);

    List<RoomInfo> getRoomsByFloorId(long id);
}
