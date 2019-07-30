package com.suql.devicecollect.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
public class RxLogin {

    @Setter
    @NotBlank
    private String userPhone;

    @Getter
    @Setter
    @NotBlank
    private String password;
}
