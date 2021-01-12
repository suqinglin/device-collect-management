package com.vians.admin.response.bean;

import lombok.Getter;
import lombok.Setter;

/**
 * 远程删除卡片响应参数体
 */
public class RmtDeleteCardResponse extends BResponse {

    @Setter
    @Getter
    private String CNT;

    @Setter
    @Getter
    private String CRC16;
}
