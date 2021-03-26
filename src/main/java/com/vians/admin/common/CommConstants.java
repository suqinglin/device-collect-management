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
    // *********************** 房间状态 ***********************/
    /**
     * 未启用
     */
    int ROOM_SERVICE_ROOM_COUNT = 0;
    /**
     * 空闲
     */
    int ROOM_STATE_IDLE = 1;
    /**
     * 入住
     */
    int ROOM_STATE_CHECK_IN = 2;
    /**
     * 冻结
     */
    int ROOM_STATE_FROZEN = 3;
    /**
     * 维修
     */
    int ROOM_STATE_REPAIR = 4;
    /**
     * 在线
     */
    int DEV_STATE_ONLINE = 1;
    /**
     * 离线
     */
    int DEV_STATE_OFFLINE = 0;
    // *********************** 授权类型 ***********************/
    /**
     * 密码授权
     */
    int AUTHORIZE_TYPE_PASSWORD = 1;
    /**
     * 卡片授权
     */
    int AUTHORIZE_TYPE_CARD = 2;
    /**
     * 指纹授权
     */
    int AUTHORIZE_TYPE_FP = 3;
    /**
     * 人脸授权
     */
    int AUTHORIZE_TYPE_FACE = 4;
    /**
     * 蓝牙授权
     */
    int AUTHORIZE_TYPE_BLUETOOTH = 5;
    // *********************** 授权内容类型 ***********************/
    /**
     *  指纹模板
     */
     int AUTHORIZE_CONTENT_TYPE_FP_TEMP = 1;
     /**
     * 指纹图片
     */
    int AUTHORIZE_CONTENT_TYPE_FP_IMAGE = 2;
    /**
     * 人脸模板
     */
    int AUTHORIZE_CONTENT_TYPE_FACE_TEMP = 3;
    /**
     * 人脸图片
     */
    int AUTHORIZE_CONTENT_TYPE_FACE_IMAGE = 4;
    // *********************** 授权时效类型 ***********************/
    /**
     * 单次
     */
    int TIME_TYPE_ONCE = 0;
    /**
     * 永久
     */
    int TIME_TYPE_FOREVER = 1;
    /**
     * 时间段
     */
    int TIME_TYPE_PERIOD = 2;
}
