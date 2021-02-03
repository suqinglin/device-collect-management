package com.vians.admin.mapper;

import com.vians.admin.model.DataDir;
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

    List<RoomInfo> getRoomList(@Param("roomName") String roomName,
                               @Param("floorId") long floorId,
                               @Param("projectId") Long projectId);

    void deleteRoom(long id);

    void editRoom(RoomInfo roomInfo);

    List<RoomModelInfo> getRoomModels();

    RoomInfo getRoomByNameInFloor(@Param("roomName") String roomName, @Param("floorId") long floorId);

    RoomInfo getRoomById(long id);

    List<RoomInfo> getRoomsByFloorId(@Param("id")long id);

    List<DataDir> getDataDir(@Param("id")long id);

    void updateRoomState(@Param("id") long id, @Param("state") int state);

    List<RoomInfo> getRoomListByUserId(@Param("id") long id);

    int getRoomCount(@Param("projectId") long projectId, @Param("state") int state);
}
