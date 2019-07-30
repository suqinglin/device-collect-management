package com.suql.devicecollect.service;

import com.suql.devicecollect.model.DeviceInfo;

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
}
