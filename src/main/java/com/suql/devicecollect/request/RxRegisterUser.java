package com.suql.devicecollect.request;

import lombok.Getter;
import lombok.Setter;

public class RxRegisterUser {

    @Getter
    @Setter
    private String userPhone;

    @Getter
    @Setter
    private String password;

    @Getter
    @Setter
    private String nickName;
}
