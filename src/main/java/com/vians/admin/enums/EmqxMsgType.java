package com.vians.admin.enums;

/**
 * mqtt消息类型
 *
 * @author plutobe@qq.com
 * @date 2019-12-05 14:44
 */
public enum EmqxMsgType {

    /**
     * 告警日志
     */
    ALARM_LOG,

    /**
     * 正常日志
     */
    NORMAL_LOG,

    /**
     * 升级开始
     */
    UPGRADE_START,

    /**
     * 升级完成
     */
    UPGRADE_DONE

}
