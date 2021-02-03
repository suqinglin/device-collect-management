package com.vians.admin.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 远程开锁请求体
 */
@ToString
public class RxRmtUnlock {

    @Getter
    @Setter
    private String userName;

    @Getter
    @Setter
    private String mac;

    /**
     * Index 为锁序号
     */
    @Getter
    @Setter
    private String index;
}
