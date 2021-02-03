package com.vians.admin.common;

public interface RedisKeyConstants {

    String VIANS_BASE_KEY = "vians-api:";

    /**
     * 口令：128 位随机值，用于该用户后续的所有访问
     */
//    String VIANS_TOKEN = VIANS_BASE_KEY + "token";
    /**
     * 计数值：每个用户单独保存，第一次为小于 0x80000000 的随
     * 机值（避免溢出），然后递增，并保存最后一次的值，每次由用户将 CNT
     * 加 1，服务器收到后更新
     */
//    String VIANS_CNT = VIANS_BASE_KEY + "CNT";
    /**
     * 用户ID
     */
//    String VIANS_USER_ID = VIANS_BASE_KEY + "userId";
    /**
     * 保存指定设备的UUID，以免频繁访问时每次都要查数据库
     */
    String DEVICE_MAC_UUID = "device-mac-uuid:";
}
