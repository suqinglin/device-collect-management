package com.vians.admin.request;

import lombok.Getter;
import lombok.Setter;

/**
 * 查询文件是否下发成功请求参数体
 */
public class RxCheckFileAck {

    @Getter
    @Setter
    private String mac;

    @Getter
    @Setter
    private String crc16;
}
