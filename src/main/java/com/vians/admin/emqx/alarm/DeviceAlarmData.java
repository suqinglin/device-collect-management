package com.vians.admin.emqx.alarm;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 日志数据
 * Created by wangkun23 on 2019/11/26.
 */
@ToString
public class DeviceAlarmData {

    /**
     * 是否有告警
     */
    @Setter
    @Getter
    private Boolean alarm = false;

    /**
     * 密码开门
     */
    @Setter
    @Getter
    private Boolean passWord = false;

    /**
     * 使用指纹开门
     */
    @Setter
    @Getter
    private Boolean fingerPrint = false;

    /**
     * 使用门卡开门
     */
    @Setter
    @Getter
    private Boolean doorCard = false;

    /**
     * 使用蓝牙开门(人脸就是蓝牙开门)
     */
    @Setter
    @Getter
    private Boolean bluetooth = false;

    /**
     * 使用远程开门
     */
    @Setter
    @Getter
    private Boolean remote = false;

    /**
     * 锁具告警日志
     * 门未未上锁
     */
    @Setter
    @Getter
    private Boolean lockNotLocked = false;

    /**
     * 锁具告警日志
     * 门未关闭
     */
    @Setter
    @Getter
    private Boolean lockNotClose = false;
    /**
     * 锁具告警日志
     * 门被撬开 防盗功能
     */
    @Setter
    @Getter
    private Boolean lockPrized = false;

    /**
     * 锁具告警日志
     * 门铃
     */
    @Setter
    @Getter
    private Boolean lockDoorbell = false;

    /**
     * 锁具告警日志
     * 键盘输入锁定
     */
    @Setter
    @Getter
    private Boolean lockKeyboardInputLock = false;

    /**
     * 锁具告警日志
     * 门锁电量过低
     */
    @Setter
    @Getter
    private Boolean lockLowElectricity = false;

    ////////// 人数计数器 start //////////
    /**
     * 人数计数器 总人数
     */
    @Setter
    @Getter
    private Integer humanSum = 0;

    /**
     * 人数计数器 进
     */
    @Setter
    @Getter
    private Integer humanIn = 0;

    /**
     * 人数计数器 出
     */
    @Setter
    @Getter
    private Integer humanOut = 0;

    /**
     * 人数计数器 方向  0出 1进
     */
    @Setter
    @Getter
    private Integer dir = 0;

    /**
     * 人数计数器 触发状态 1触发
     */
    @Setter
    @Getter
    private Integer status = 0;
    ////////// 人数计数器 end //////////

}
