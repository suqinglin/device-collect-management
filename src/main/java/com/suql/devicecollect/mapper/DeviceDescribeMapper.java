package com.suql.devicecollect.mapper;

import com.suql.devicecollect.model.DeviceDescribeInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DeviceDescribeMapper {

    DeviceDescribeInfo getDeviceDescribeInfoByDescribe(String describe);
}
