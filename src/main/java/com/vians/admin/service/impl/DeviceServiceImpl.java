package com.vians.admin.service.impl;

import com.vians.admin.mapper.DeviceMapper;
import com.vians.admin.model.DeviceInfo;
import com.vians.admin.service.DeviceService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigInteger;

@Service
public class DeviceServiceImpl implements DeviceService {

    @Resource
    private DeviceMapper deviceMapper;

    @Override
    public DeviceInfo getDeviceInfoByMac(String mac) {
        BigInteger macNum = new BigInteger(mac, 16);
        return deviceMapper.getDeviceByMac(macNum);
    }
}
