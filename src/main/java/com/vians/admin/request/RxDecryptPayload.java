package com.vians.admin.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

public class RxDecryptPayload {

    @Getter
    @Setter
    @NotBlank
    private String mac;

    @Getter
    @Setter
    @NotBlank
    private String respStr;
}
