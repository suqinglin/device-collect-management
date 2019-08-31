package com.suql.devicecollect.request;

import lombok.Getter;
import lombok.Setter;

public class RxManufList {

    @Getter
    @Setter
    private int pageIndex = 1;

    @Getter
    @Setter
    private int pageSize = 30;
}
