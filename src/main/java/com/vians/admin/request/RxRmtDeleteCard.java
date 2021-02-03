package com.vians.admin.request;

import lombok.Getter;
import lombok.Setter;

/**
 * 远程删除卡片请求体
 */
public class RxRmtDeleteCard {

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
    private String cardNum;

    @Getter
    @Setter
    private String cardIdx;

}
