package com.suql.devicecollect.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;

public class DeviceInfo {

    @Getter
    @Setter
    private long id;

    @Getter
    @Setter
    private String uuid;

    @Getter
    private String mac;

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

    private BigInteger macNum;

    private long snNum;

    public void setMacNum(BigInteger macNum) {
        this.macNum = macNum;
        mac = String.format("%016X", macNum);
    }

    public BigInteger getMacNum() {
        return macNum;
    }

    public void setSnNum(long snNum) {
        this.snNum = snNum;
        sn = String.format("%010d", snNum);
    }

    public long getSnNum() {
        return snNum;
    }
}
