package com.vians.admin.service;

import com.vians.admin.model.DeviceBaseInfo;
import com.vians.admin.model.DeviceDetailInfo;
import com.vians.admin.model.DeviceInfo;
import com.vians.admin.request.query.DeviceQuery;
import com.vians.admin.response.Page;

import java.util.List;

public interface DeviceService {


    DeviceInfo getDeviceInfoByMac(String mac);

    void addDevice(DeviceBaseInfo deviceBaseInfo);

    Page<DeviceDetailInfo> getDevicesByPage(DeviceQuery deviceQuery);

    List<DeviceBaseInfo> getFreeDevices();

    List<DeviceBaseInfo> getBindedDevices(long roomId);

    void bindDevice(long roomId,
                    long deviceId,
                    long userId);

    void deleteDevice(long id);

    void unbindDevice(long id);
}
