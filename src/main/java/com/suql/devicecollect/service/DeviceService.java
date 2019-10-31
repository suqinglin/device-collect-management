package com.suql.devicecollect.service;

import com.suql.devicecollect.model.DeviceInfo;
import com.suql.devicecollect.response.Page;
import com.suql.devicecollect.response.Pageable;

import java.util.List;

public interface DeviceService {

    DeviceInfo getDeviceInfoByUuid(String uuid);

    DeviceInfo getDeviceInfoByMac(String mac);

    String getNextMac();

    String getNextSn(String model);

    void saveMacAndToken(long userId, String uuid, String mac, String token);

    void createDevice(String sn, String mac, String token, String model);

    void updateDevice(long id, String model, String HwVer, String manufacturer);

    void updateDevice(DeviceInfo deviceInfo);

    void deleteDeviceByMac(String mac);

    List<String> getModelGroup();

    Page<DeviceInfo> findListByModel(Pageable pageable, String model, String sn);

    void deleteDevices(List<String> macs);

    /**
     * 曼瑜锁2号版本的设备，由于程序问题，不能扫码生产，但仍需将读取到的设备信息保存到服务器端数据库
     * 为区分正常设备的数据，state为3
     */
    void saveManyuLock2(String uuid, String mac, String model, String token, String hwVer, String fwVer);
}
