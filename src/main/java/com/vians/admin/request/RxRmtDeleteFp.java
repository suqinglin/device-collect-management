package com.vians.admin.request;

import lombok.Getter;
import lombok.Setter;

/**
 * 远程删除指纹请求体
 */
public class RxRmtDeleteFp {

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
}
