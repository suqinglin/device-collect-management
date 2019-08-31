package com.suql.devicecollect.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class RxDeleteDevices {

    @Getter
    @Setter
    private List<String> macs;
}
