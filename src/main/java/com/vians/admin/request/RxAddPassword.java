package com.vians.admin.request;

import lombok.Getter;
import lombok.Setter;

/**
 * @ClassName RxAddPassword
 * @Description TODO
 * @Author su qinglin
 * @Date 2021/1/18 22:36
 * @Version 1.0
 **/
public class RxAddPassword {

    @Getter
    @Setter
    private long userId;

    @Getter
    @Setter
    private String userName;

    @Getter
    @Setter
    private long roomId;

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
