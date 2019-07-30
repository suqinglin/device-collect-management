package com.suql.devicecollect.mapper;

import com.suql.devicecollect.model.DeviceInfo;
import org.apache.ibatis.annotations.Mapper;

import java.math.BigInteger;

@Mapper
public interface DeviceMapper {

    DeviceInfo getDeviceByUuid(String uuid);

    DeviceInfo getDeviceByMac(BigInteger mac);

    String getMaxMacNum();

    String getMaxSnNum(String model);

    void saveDeviceInfo(DeviceInfo device);

    String getMaxId();

    void update(DeviceInfo deviceInfo);

    void deleteDeviceByMac(BigInteger mac);
}