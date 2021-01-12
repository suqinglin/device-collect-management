package com.vians.admin.response.bean;

import lombok.Getter;
import lombok.Setter;

/**
 * 远程下发人脸模板响应参数体
 */
public class RmtAddFaceResponse extends BResponse {

    @Setter
    @Getter
    private String CNT;

    @Setter
    @Getter
    private String CRC16;
}
