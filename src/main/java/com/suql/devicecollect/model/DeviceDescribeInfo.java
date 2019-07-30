package com.suql.devicecollect.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
public class DeviceDescribeInfo {

    @Getter
    @Setter
    private long id;

    @Getter
    @Setter
    private String smallDescribe;

    @Getter
    @Setter
    private String typeValue;

    @Getter
    @Setter
    private int state;

    @Getter
    @Setter
    private String bigDescribe;
}
