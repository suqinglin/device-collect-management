package com.suql.devicecollect.service.impl;

import com.suql.devicecollect.mapper.DeviceDescribeMapper;
import com.suql.devicecollect.model.DeviceDescribeInfo;
import com.suql.devicecollect.service.DeviceDescribeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class DeviceDescribeServiceImpl implements DeviceDescribeService {

    @Resource
    private DeviceDescribeMapper deviceDescribeMapper;

    @Override
    public String getDeviceRemarkByDescribe(String describe) {
        DeviceDescribeInfo deviceDescribeInfo = deviceDescribeMapper.getDeviceDescribeInfoByDescribe(describe);
        if (deviceDescribeInfo == null) {
            return null;
        }
        return deviceDescribeInfo.getBigDescribe();
    }
}
