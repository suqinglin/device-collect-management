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

    DeviceInfo getDeviceByMac(BigInteger mac);

    void addDevice(DeviceBaseInfo deviceBaseInfo);

    List<DeviceDetailInfo> getDeviceList(DeviceQuery deviceQuery);

    /**
     * 获取空闲设备，即未绑定到房间的设备
     * @return
     */
    List<DeviceBaseInfo> getFreeDevices();

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

    void deleteDevice(@Param("id") long id);

    void unbindDevice(long id);
}
