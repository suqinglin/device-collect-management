package com.vians.admin.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.vians.admin.mapper.DeviceMapper;
import com.vians.admin.model.DeviceBaseInfo;
import com.vians.admin.model.DeviceDetailInfo;
import com.vians.admin.model.DeviceInfo;
import com.vians.admin.request.query.DeviceQuery;
import com.vians.admin.response.Page;
import com.vians.admin.response.Pageable;
import com.vians.admin.service.DeviceService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

@Service
public class DeviceServiceImpl implements DeviceService {

    @Resource
    private DeviceMapper deviceMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public DeviceInfo getDeviceInfoByMac(String mac) {
        BigInteger macNum = new BigInteger(mac, 16);
        return deviceMapper.getDeviceByMac(macNum);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addDevice(DeviceBaseInfo deviceBaseInfo) {
        deviceMapper.addDevice(deviceBaseInfo);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Page<DeviceDetailInfo> getDevicesByPage(DeviceQuery deviceQuery) {
        Pageable pageable = new Pageable(deviceQuery.getPageIndex(), deviceQuery.getPageSize());
        PageHelper.startPage(pageable.getPageNumber(), pageable.getPageSize());
        List<DeviceDetailInfo> deviceDetailInfoList = deviceMapper.getDeviceList(deviceQuery);
        PageInfo<DeviceDetailInfo> pageInfo =  new PageInfo<>(deviceDetailInfoList);
        return new Page<>(deviceDetailInfoList, pageInfo.getTotal(), pageable);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<DeviceBaseInfo> getFreeDevices() {
        return deviceMapper.getFreeDevices();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<DeviceBaseInfo> getBindedDevices(long roomId) {
        return deviceMapper.getBindedDevice(roomId);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void bindDevice(long roomId, long deviceId, long userId) {
        deviceMapper.bindDevice(roomId, deviceId, userId, new Date());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteDevice(long id) {
        deviceMapper.deleteDevice(id);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void unbindDevice(long id) {
        deviceMapper.unbindDevice(id);
    }
}
