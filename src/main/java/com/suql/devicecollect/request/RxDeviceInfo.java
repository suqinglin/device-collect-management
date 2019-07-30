package com.suql.devicecollect.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

public class RxDeviceInfo {

    @Getter
    @Setter
    private String uuid;

    @NotBlank
    @Setter
    @Getter
    private String mac;

    @Setter
    @Getter
    private String sn;

    @Getter
    @Setter
    private String model;

    /**
     * 硬件版本
     */
    @Getter
    @Setter
    private String hwVersion;
    /**
     * 固件版本
     */
    @Getter
    @Setter
    private String fwVersion;

    /**
     * 制造商
     */
    @Getter
    @Setter
    private String manufacturer;

    @Getter
    @Setter
    private int toolId;

    /**
     * 创建时间
     */
    @Getter
    @Setter
    private long createTime;

    /**
     * 创建者ID
     */
    @Getter
    @Setter
    private long userId;

    @Getter
    @Setter
    private String token;
}
