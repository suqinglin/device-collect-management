package com.suql.devicecollect.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.util.List;

public class RxCancelPrint {

    @NotBlank
    @Getter
    @Setter
    private List<String> macList;
}
