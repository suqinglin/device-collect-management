package com.vians.admin.common;

import com.vians.admin.model.DeviceInfo;
import com.vians.admin.service.DeviceService;
import com.vians.admin.service.RedisService;
import org.springframework.util.StringUtils;

/**
 * @ClassName DeviceHelper
 * @Description TODO
 * @Author su qinglin
 * @Date 2021/6/2 18:09
 * @Version 1.0
 **/
public class DeviceHelper {

    /**
     * 根据设备MAC获取UUID
     * @param mac
     * @return
     */
    public static String getUUID(DeviceService deviceService, RedisService redisService, String mac) {
        String UUID = redisService.get(RedisKeyConstants.DEVICE_MAC_UUID + mac);
        if (StringUtils.isEmpty(UUID)) {
            DeviceInfo deviceInfo = deviceService.getDeviceInfoByMac(mac.contains("FFFF") ? mac : "FFFF" + mac);
            if (deviceInfo == null) {
                return null;
            }
            UUID = deviceInfo.getUuid();
            redisService.set(RedisKeyConstants.DEVICE_MAC_UUID + mac, UUID);
        }
        return UUID;
    }
}
