package com.suql.devicecollect.model;

import lombok.Getter;
import lombok.Setter;

/**
 * 厂商信息
 */
public class ManufInfo {

    @Getter
    @Setter
    private long id;

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private String address;

    @Getter
    @Setter
    private String password;

    @Getter
    @Setter
    private long createTime;

    @Getter
    @Setter
    private long createUserId;

    @Getter
    @Setter
    private int state;
}
