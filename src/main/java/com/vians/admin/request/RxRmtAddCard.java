package com.vians.admin.request;

import lombok.Getter;
import lombok.Setter;

/**
 * 远程下发卡片请求参数
 */
public class RxRmtAddCard {

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

    @Getter
    @Setter
    private String cardType;

    @Getter
    @Setter
    private String beginTime;

    @Getter
    @Setter
    private String endTime;

}
