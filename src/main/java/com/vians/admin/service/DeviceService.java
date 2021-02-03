package com.vians.admin.service;

import com.vians.admin.model.DeviceBaseInfo;
import com.vians.admin.model.DeviceDetailInfo;
import com.vians.admin.model.DeviceInfo;
import com.vians.admin.request.RxPage;
import com.vians.admin.request.query.DeviceQuery;
import com.vians.admin.response.Page;

import java.util.List;

public interface DeviceService {


    DeviceInfo getDeviceInfoByMac(String mac);

    void addDevice(DeviceBaseInfo deviceBaseInfo);

    Page<DeviceDetailInfo> getDevicesByPage(DeviceQuery deviceQuery);

    Page<DeviceBaseInfo> getFreeDevicesByPage(RxPage page, Long projectId);

    List<DeviceBaseInfo> getBindedDevices(long roomId);

    void bindDevice(long roomId,
                    long deviceId,
                    long userId);

    void deleteDeviceById(long id);

    void deleteDeviceByMac(String mac);

    void unbindDevice(long id);

    DeviceBaseInfo getLockFromRoom(long roomId);

    DeviceBaseInfo getDeviceByRoomId(long roomId);

    List<DeviceBaseInfo> getCollectDevicesByUser(long userId);

    void batchAddUsers(List<DeviceBaseInfo> deviceList, Long userId);

    List<String> getDeviceMacs(long projectId);

    int getDevCount(long projectId, int state);

    int getGwCount(long rootId, int state);
}
