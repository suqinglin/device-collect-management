package com.suql.devicecollect.mapper;

import com.suql.devicecollect.model.DeviceInfo;
import org.apache.ibatis.annotations.Mapper;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

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

    List<String> getModelGroup();

    List<DeviceInfo> findListByModel(DeviceInfo deviceInfo);

    String getNewSn(String model);

    String getNewMacNum();
}
