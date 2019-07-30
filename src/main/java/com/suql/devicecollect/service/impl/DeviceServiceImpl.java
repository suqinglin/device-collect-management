package com.suql.devicecollect.service.impl;

import com.suql.devicecollect.Constants;
import com.suql.devicecollect.mapper.DeviceMapper;
import com.suql.devicecollect.model.DeviceInfo;
import com.suql.devicecollect.service.DeviceService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigInteger;

@Service
public class DeviceServiceImpl implements DeviceService {

    @Resource
    private DeviceMapper deviceMapper;

    @Override
    public DeviceInfo getDeviceInfoByUuid(String uuid) {
        return deviceMapper.getDeviceByUuid(uuid);
    }

    @Override
    public DeviceInfo getDeviceInfoByMac(String mac) {
        BigInteger macNum = new BigInteger(mac, 16);
        return deviceMapper.getDeviceByMac(macNum);
    }

    @Override
    public String getNextMac() {
        if (deviceMapper.getMaxMacNum() == null) {
            return Constants.MAC_START + "000001";
        } else {
            BigInteger maxMacNum = new BigInteger(deviceMapper.getMaxMacNum());
            return String.format("%016X", maxMacNum.add(BigInteger.ONE));
        }
    }

    @Override
    public String getNextSn(String model) {
        String maxSnNum = deviceMapper.getMaxSnNum(model);
        long nextSn;
        if (maxSnNum == null) {
            nextSn = 1;
        } else {
            nextSn = Long.valueOf(maxSnNum) + 1;
        }
        return String.format("%010d", nextSn);
    }

    @Override
    public void saveMacAndToken(long userId, String uuid, String mac, String token) {
        long id = 0;
        if (deviceMapper.getMaxId() != null) {
            id = Long.valueOf(deviceMapper.getMaxId()) + 1;
        }
        DeviceInfo deviceInfo = new DeviceInfo();
        deviceInfo.setId(id);
        deviceInfo.setUuid(uuid);
        deviceInfo.setMacNum(new BigInteger(mac, 16));
        deviceInfo.setToken(token);
        deviceInfo.setCreateTime(System.currentTimeMillis());
        deviceInfo.setUserId(userId);
        deviceMapper.saveDeviceInfo(deviceInfo);
    }

    @Override
    public void createDevice(String sn, String mac, String token, String model) {
        long id = 0;
        if (deviceMapper.getMaxId() != null) {
            id = Long.valueOf(deviceMapper.getMaxId()) + 1;
        }
        DeviceInfo deviceInfo = new DeviceInfo();
        deviceInfo.setId(id);
        deviceInfo.setSnNum(Long.parseLong(sn));
        deviceInfo.setMacNum(new BigInteger(mac, 16));
        deviceInfo.setToken(token);
        deviceInfo.setModel(model);
        deviceInfo.setCreateTime(System.currentTimeMillis() / 1000);
        deviceMapper.saveDeviceInfo(deviceInfo);
    }

    @Override
    public void updateDevice(long id, String model, String HwVer, String manufacturer) {
        DeviceInfo deviceInfo = new DeviceInfo();
        deviceInfo.setId(id);
        deviceInfo.setModel(model);
        deviceInfo.setHwVersion(HwVer);
        deviceInfo.setManufacturer(manufacturer);
        deviceMapper.update(deviceInfo);
    }

    @Override
    public void updateDevice(DeviceInfo deviceInfo) {
//        BigInteger macNum = new BigInteger(mac, 16);
//        DeviceInfo device = deviceMapper.getDeviceByMac(macNum);
//        if (!StringUtils.isEmpty(deviceInfo.getUuid())) {
//            device.setUuid(deviceInfo.getUuid());
//        }
//        if (!StringUtils.isEmpty(deviceInfo.getToken())) {
//            device.setToken(deviceInfo.getToken());
//        }
//        if (!StringUtils.isEmpty(deviceInfo.getSn())) {
//            device.setSnNum(Long.valueOf(deviceInfo.getSn()));
//        }
//        if (!StringUtils.isEmpty(deviceInfo.getModel())) {
//            device.setModel(deviceInfo.getModel());
//        }
//        if (!StringUtils.isEmpty(deviceInfo.getHwVersion())) {
//            device.setHwVersion(deviceInfo.getHwVersion());
//        }
//        if (!StringUtils.isEmpty(deviceInfo.getFwVersion())) {
//            device.setFwVersion(deviceInfo.getFwVersion());
//        }
//        if (!StringUtils.isEmpty(deviceInfo.getManufacturer())) {
//            device.setManufacturer(deviceInfo.getManufacturer());
//        }
//        if (deviceInfo.getToolId() != 0) {
//            device.setToolId(deviceInfo.getToolId());
//        }
//        if (deviceInfo.getCreateTime() != 0) {
//            device.setCreateTime(deviceInfo.getCreateTime());
//        }
//        if (deviceInfo.getUserId() != 0) {
//            device.setUserId(deviceInfo.getUserId());
//        }
        deviceMapper.update(deviceInfo);
    }

    @Override
    public void deleteDeviceByMac(String mac) {
        deviceMapper.deleteDeviceByMac(new BigInteger(mac, 16));
    }
}