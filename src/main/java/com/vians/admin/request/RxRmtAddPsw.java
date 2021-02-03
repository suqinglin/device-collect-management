package com.vians.admin.request;

import lombok.Getter;
import lombok.Setter;

/**
 * 远程下发密码请求体
 */
public class RxRmtAddPsw {

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
    private String psw;

    @Getter
    @Setter
    private String pswIdx;

    @Getter
    @Setter
    private String pswType;

    @Getter
    @Setter
    private String beginTime;

    @Getter
    @Setter
    private String endTime;

}
