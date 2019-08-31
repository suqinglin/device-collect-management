package com.suql.devicecollect.request;

import lombok.Getter;
import lombok.Setter;

public class RxDeviceListByModel {

    @Getter
    @Setter
    private String model;

    @Getter
    @Setter
    private String sn;

    @Getter
    @Setter
    private int pageIndex = 1;

    @Getter
    @Setter
    private int pageSize = 30;
}
