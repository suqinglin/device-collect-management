package com.vians.admin.request;

import lombok.Getter;
import lombok.Setter;

/**
 * 远程删除人脸请求体
 */
public class RxRmtDeleteFace {

    @Getter
    @Setter
    private String mac;

    @Getter
    @Setter
    private String index;

    @Getter
    @Setter
    private String faceIdx;
}
