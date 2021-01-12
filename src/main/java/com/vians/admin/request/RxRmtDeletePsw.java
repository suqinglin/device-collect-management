package com.vians.admin.request;

import lombok.Getter;
import lombok.Setter;

/**
 * 远程删除密码请求体
 */
public class RxRmtDeletePsw {

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

}
