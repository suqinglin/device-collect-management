package com.vians.admin.mapper;

import com.vians.admin.model.DeviceBaseInfo;
import com.vians.admin.model.DeviceDetailInfo;
import com.vians.admin.model.DeviceInfo;
import com.vians.admin.request.query.DeviceQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

@Mapper
public interface DeviceMapper {

    List<DeviceBaseInfo> getCollectDevicesByUser(@Param("userId") long userId);

    DeviceInfo getDeviceByMac(BigInteger mac);

    DeviceBaseInfo getDeviceByRoomId(@Param("roomId") long roomId);

    void addDevice(DeviceBaseInfo deviceBaseInfo);

    List<DeviceDetailInfo> getDeviceList(DeviceQuery deviceQuery);

    /**
     * 获取空闲设备，即未绑定到房间的设备
     * @return
     * @param projectId
     */
    List<DeviceBaseInfo> getFreeDevices(@Param("projectId") long projectId);

    /**
     * 获取指定房间的已绑定设备
     * @param roomId
     * @return
     */
    List<DeviceBaseInfo> getBindedDevice(@Param("roomId") long roomId);

    void bindDevice(@Param("roomId") long roomId,
                    @Param("deviceId") long deviceId,
                    @Param("userId") long userId,
                    @Param("bindTime") Date bindTime);

    void deleteDeviceById(@Param("id") long id);

    void deleteDeviceByMac(@Param("mac") String mac);

    void unbindDevice(long id);

    DeviceBaseInfo getLockFromRoom(@Param("roomId") long roomId);

    List<String> getDeviceMacsByProject(@Param("projectId") long projectId);

    int getDevCount(@Param("projectId") long projectId, @Param("state") int state);

    int getGwCount(@Param("rootId") long rootId, @Param("state") int state);

    void unbindDevicesByRoomId(@Param("roomId") long roomId);

    void addBrowserDevice(@Param("browserUUID") String browserUUID, @Param("mac") String mac, @Param("userId") long userId);

    String getDeviceMacByBrowser(@Param("browserUUID") String browserUUID);
}
