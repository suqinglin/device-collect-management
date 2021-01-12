package com.vians.admin.mapper;

import com.vians.admin.model.RoomInfo;
import com.vians.admin.model.RoomModelInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @ClassName RoomMapper
 * @Description TODO
 * @Author su qinglin
 * @Date 2021/1/8 11:44
 * @Version 1.0
 **/
@Mapper
public interface RoomMapper {

    void addRoom(RoomInfo floorInfo);

    List<RoomInfo> getRoomList(@Param("roomName") String roomName, @Param("floorId") long floorId);

    void deleteRoom(long id);

    void editRoom(RoomInfo roomInfo);

    List<RoomModelInfo> getRoomModels();

    RoomInfo getRoomByNameInFloor(@Param("roomName") String roomName, @Param("floorId") long floorId);

    RoomInfo getRoomById(long id);
}
