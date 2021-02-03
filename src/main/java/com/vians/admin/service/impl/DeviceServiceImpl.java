package com.vians.admin.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.vians.admin.mapper.DeviceMapper;
import com.vians.admin.model.DeviceBaseInfo;
import com.vians.admin.model.DeviceDetailInfo;
import com.vians.admin.model.DeviceInfo;
import com.vians.admin.request.RxPage;
import com.vians.admin.request.query.DeviceQuery;
import com.vians.admin.response.Page;
import com.vians.admin.response.Pageable;
import com.vians.admin.service.DeviceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

@Service
public class DeviceServiceImpl implements DeviceService {

    final Logger logger = LoggerFactory.getLogger(getClass());

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

    /**
     * 分页获取空闲设备
     * @param page
     * @param projectId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Page<DeviceBaseInfo> getFreeDevicesByPage(RxPage page, Long projectId) {
        Pageable pageable = new Pageable(page.getPageIndex(), page.getPageSize());
        PageHelper.startPage(pageable.getPageNumber(), pageable.getPageSize());
        List<DeviceBaseInfo> deviceBaseInfoList = deviceMapper.getFreeDevices(projectId);
        PageInfo<DeviceBaseInfo> pageInfo = new PageInfo<>(deviceBaseInfoList);
        return new Page<>(deviceBaseInfoList, pageInfo.getTotal(), pageable);
    }

    /**
     * 获取指定房间下的已绑定设备
     * @param roomId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<DeviceBaseInfo> getBindedDevices(long roomId) {
        return deviceMapper.getBindedDevice(roomId);
    }

    /**
     * 绑定设备
     * @param roomId
     * @param deviceId
     * @param userId
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void bindDevice(long roomId, long deviceId, long userId) {
        deviceMapper.bindDevice(roomId, deviceId, userId, new Date());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteDeviceById(long id) {
        deviceMapper.deleteDeviceById(id);
    }

    @Override
    public void deleteDeviceByMac(String mac) {
        deviceMapper.deleteDeviceByMac(mac);
    }

    /**
     * 解绑设备
     * @param id
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void unbindDevice(long id) {
        deviceMapper.unbindDevice(id);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public DeviceBaseInfo getLockFromRoom(long roomId) {
        return deviceMapper.getLockFromRoom(roomId);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public DeviceBaseInfo getDeviceByRoomId(long roomId) {
        return deviceMapper.getDeviceByRoomId(roomId);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<DeviceBaseInfo> getCollectDevicesByUser(long userId) {
        return deviceMapper.getCollectDevicesByUser(userId);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void batchAddUsers(List<DeviceBaseInfo> deviceList, Long userId) {
        deviceList.forEach(device -> {
            if (device.getMAC() == null) {
                logger.info("deviceService mac is null, {}", device.toString());
                return;
            }
            device.setCreateTime(new Date());
            device.setUserId(String.valueOf(userId));
            deviceMapper.addDevice(device);
        });
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<String> getDeviceMacs(long projectId) {
        return deviceMapper.getDeviceMacsByProject(projectId);
    }

    @Override
    public int getDevCount(long projectId, int state) {
        return deviceMapper.getDevCount(projectId, state);
    }

    @Override
    public int getGwCount(long rootId, int state) {
        return deviceMapper.getGwCount(rootId, state);
    }
}
