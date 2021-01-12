package com.vians.admin.response.bean;

import lombok.Getter;
import lombok.Setter;

/**
 * 远程删除人脸响应参数体
 */
public class RmtDeleteFaceResponse extends BResponse {

    @Setter
    @Getter
    private String CNT;

    @Setter
    @Getter
    private String CRC16;
}
