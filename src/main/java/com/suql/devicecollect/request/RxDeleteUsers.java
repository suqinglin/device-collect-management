package com.suql.devicecollect.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class RxDeleteUsers {

    @Getter
    @Setter
    private List<Integer> userIds;
}
