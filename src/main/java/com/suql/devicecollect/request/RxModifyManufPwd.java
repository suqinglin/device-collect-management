package com.suql.devicecollect.request;

import lombok.Getter;
import lombok.Setter;

public class RxModifyManufPwd {

    @Getter
    @Setter
    private int manufId;

    @Getter
    @Setter
    private String oldPwd;

    @Getter
    @Setter
    private String newPwd;
}
