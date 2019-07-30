package com.suql.devicecollect.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

public class RxGetDeviceToken {

    @Getter
    @Setter
    @NotBlank
    private String mac;
}
