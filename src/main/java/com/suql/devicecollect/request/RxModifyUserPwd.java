package com.suql.devicecollect.request;

import lombok.Getter;
import lombok.Setter;

public class RxModifyUserPwd {

    @Getter
    @Setter
    private int userId;

    @Getter
    @Setter
    private String oldPwd;

    @Getter
    @Setter
    private String newPwd;
}
