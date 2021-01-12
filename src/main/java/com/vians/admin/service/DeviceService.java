package com.vians.admin.service;

import com.vians.admin.model.DeviceInfo;

public interface DeviceService {


    DeviceInfo getDeviceInfoByMac(String mac);
}
