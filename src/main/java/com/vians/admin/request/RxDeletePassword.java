package com.vians.admin.request;

import lombok.Getter;
import lombok.Setter;

/**
 * @ClassName RxDeletePassword
 * @Description TODO
 * @Author su qinglin
 * @Date 2021/1/19 18:12
 * @Version 1.0
 **/
public class RxDeletePassword {

    @Getter
    @Setter
    private long id;

    @Getter
    @Setter
    private long roomId;

    @Getter
    @Setter
    private String psw;

    @Getter
    @Setter
    private String pswIdx;
}
