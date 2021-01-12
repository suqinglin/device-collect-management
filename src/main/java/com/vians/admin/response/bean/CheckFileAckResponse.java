package com.vians.admin.response.bean;

import lombok.Getter;
import lombok.Setter;

/**
 * 查询文件是否下发成功响应参数体
 */
public class CheckFileAckResponse extends BResponse {

    @Getter
    @Setter
    private String CNT;
}
