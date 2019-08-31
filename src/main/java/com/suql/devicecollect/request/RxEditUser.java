package com.suql.devicecollect.request;

import lombok.Getter;
import lombok.Setter;

public class RxEditUser {

    @Getter
    @Setter
    private int userId;

    @Getter
    @Setter
    private String nickName;

    @Getter
    @Setter
    private String account;
}
