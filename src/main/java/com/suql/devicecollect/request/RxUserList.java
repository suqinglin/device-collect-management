package com.suql.devicecollect.request;

import lombok.Getter;
import lombok.Setter;

public class RxUserList {

    @Getter
    @Setter
    private int pageIndex = 1;

    @Getter
    @Setter
    private int pageSize = 30;
}
