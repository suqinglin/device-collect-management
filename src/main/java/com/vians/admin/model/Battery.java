package com.vians.admin.model;

import lombok.Getter;
import lombok.Setter;

/**
 * 设备电量
 */
public class Battery {
    /**
     * Battery : 55
     * Time : 1599197721
     * MAC : DC2C28010295
     * Name : 门锁#1
     */
    @Getter
    @Setter
    private int Battery;

    @Getter
    @Setter
    private int Time;

    @Getter
    @Setter
    private String MAC;

    @Getter
    @Setter
    private String Name;

}
