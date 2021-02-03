package com.vians.admin.request;

import lombok.Getter;
import lombok.Setter;

/**
 * @ClassName RxAddFp
 * @Description TODO
 * @Author su qinglin
 * @Date 2021/1/18 22:36
 * @Version 1.0
 **/
public class RxAddFp {

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
    private String fpTemp;

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
    private String featureValue;

    @Getter
    @Setter
    private String beginTime;

    @Getter
    @Setter
    private String endTime;
}
