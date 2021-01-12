package com.vians.admin.mapper;

import com.vians.admin.model.DeviceInfo;
import org.apache.ibatis.annotations.Mapper;

import java.math.BigInteger;

@Mapper
public interface DeviceMapper {

    DeviceInfo getDeviceByMac(BigInteger mac);

}
