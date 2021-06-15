package com.vians.admin.emqx.alarm;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 设备告警日志推送
 * Created by wangkun23 on 2019/11/25.
 */
@ToString
public class DeviceAlarmEmqx {

    /**
     * 根据设备推送数据
     */
    @Setter
    @Getter
    private String mac;

    /**
     * 事件发生时间
     */
    @Setter
    @Getter
    private Long time;

    /**
     * 日志特性数据
     */
    @Setter
    @Getter
    private Integer featureType;

    /**
     * 数据可变数据
     */
    @Setter
    @Getter
    private DeviceAlarmData data;
}
