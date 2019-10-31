package com.suql.devicecollect.request;

import lombok.Getter;
import lombok.Setter;

public class RxSaveManyu2Lock {

    @Getter
    @Setter
    private String uuid;

    @Getter
    @Setter
    private String model;

    @Getter
    @Setter
    private String mac;

    @Getter
    @Setter
    private String token;

    @Getter
    @Setter
    private String hwVer;

    @Getter
    @Setter
    private String fwVer;
}
