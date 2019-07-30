package com.suql.devicecollect.model;

import lombok.Getter;
import lombok.Setter;

public class PrintDeviceBean {

    @Getter
    @Setter
    private String remark;

    @Getter
    @Setter
    private String mac;

    @Getter
    @Setter
    private String sn;

    public PrintDeviceBean() {
    }

    public PrintDeviceBean(String remark, String mac, String sn) {
        this.remark = remark;
        this.mac = mac;
        this.sn = sn;
    }
}
