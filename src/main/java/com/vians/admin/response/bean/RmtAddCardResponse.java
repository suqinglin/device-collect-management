package com.vians.admin.response.bean;

import lombok.Getter;
import lombok.Setter;

/**
 * 远程下发卡片响应参数体
 */
public class RmtAddCardResponse extends BResponse {

    @Setter
    @Getter
    private String CNT;

    @Setter
    @Getter
    private String CRC16;
}
