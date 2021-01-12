package com.vians.admin.request;

import lombok.Getter;
import lombok.Setter;

/**
 * 远程下发人脸请求体
 */
public class RxRmtAddFace {

    @Getter
    @Setter
    private String mac;

    @Getter
    @Setter
    private String index;

    @Getter
    @Setter
    private String faceIdx;

    @Getter
    @Setter
    private String faceType;

    @Getter
    @Setter
    private String faceFmt;

    @Getter
    @Setter
    private String faceTemp;

    @Getter
    @Setter
    private String beginTime;

    @Getter
    @Setter
    private String endTime;

}
