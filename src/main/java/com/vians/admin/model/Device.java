package com.vians.admin.model;

import lombok.Getter;
import lombok.Setter;

/**
 * 设备信息表
 */
public class Device {

    @Getter
    @Setter
    private String MAC;

    @Getter
    @Setter
    private String GwMac;

    @Getter
    @Setter
    private String Device;

    @Getter
    @Setter
    private String Active;

    @Getter
    @Setter
    private String Feature;

    @Getter
    @Setter
    private String Index;

    @Getter
    @Setter
    private String Name;
}
