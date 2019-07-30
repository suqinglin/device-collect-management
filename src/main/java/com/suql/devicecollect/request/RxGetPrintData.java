package com.suql.devicecollect.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

public class RxGetPrintData {

    @NotBlank
    @Getter
    @Setter
    private String model;

    @NotBlank
    @Getter
    @Setter
    private int count;
}
