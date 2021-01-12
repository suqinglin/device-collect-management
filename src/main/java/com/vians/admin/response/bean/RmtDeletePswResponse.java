package com.vians.admin.response.bean;

import lombok.Getter;
import lombok.Setter;

public class RmtDeletePswResponse extends BResponse {

    @Setter
    @Getter
    private String CNT;

    @Setter
    @Getter
    private String CRC16;
}
