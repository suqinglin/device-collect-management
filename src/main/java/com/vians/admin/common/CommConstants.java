package com.vians.admin.common;

/**
 * @ClassName CommConstants
 * @Description TODO
 * @Author su qinglin
 * @Date 2021/1/29 9:31
 * @Version 1.0
 **/
public interface CommConstants {

    String C_API_URL = "http://admin.szvians.cn:8087";
    // 未启用
    int ROOM_SERVICE_ROOM_COUNT = 0;
    // 空闲
    int ROOM_STATE_IDLE = 1;
    // 入住
    int ROOM_STATE_CHECK_IN = 2;
    // 冻结
    int ROOM_STATE_FROZEN = 3;
    // 维修
    int ROOM_STATE_REPAIR = 4;
    // 在线
    int DEV_STATE_ONLINE = 1;
    // 离线
    int DEV_STATE_OFFLINE = 0;
}
