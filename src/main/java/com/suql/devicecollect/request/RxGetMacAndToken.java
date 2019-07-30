package com.suql.devicecollect.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

public class RxGetMacAndToken {

    @Getter
    @Setter
    @NotBlank
    private String uuid;
}
