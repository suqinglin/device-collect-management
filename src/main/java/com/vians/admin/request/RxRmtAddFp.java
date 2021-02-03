package com.vians.admin.request;

import lombok.Getter;
import lombok.Setter;

/**
 * 远程下发指纹请求体
 */
public class RxRmtAddFp {

    @Getter
    @Setter
    private String userName;

    @Getter
    @Setter
    private String mac;

    @Getter
    @Setter
    private String index;

    @Getter
    @Setter
    private String fpIdx;

    @Getter
    @Setter
    private String fpType;

    @Getter
    @Setter
    private String fpFmt;

    @Getter
    @Setter
    private String fpTemp;

    @Getter
    @Setter
    private String beginTime;

    @Getter
    @Setter
    private String endTime;

}
