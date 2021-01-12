package com.vians.admin.response.bean;

import lombok.Getter;
import lombok.Setter;

/**
 * 远程下发指纹模板响应参数体
 */
public class RmtAddFpResponse extends BResponse {

    @Setter
    @Getter
    private String CNT;

    @Setter
    @Getter
    private String CRC16;
}
