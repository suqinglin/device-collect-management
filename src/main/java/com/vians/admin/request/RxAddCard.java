package com.vians.admin.request;

import lombok.Getter;
import lombok.Setter;

/**
 * @ClassName RxAddCard
 * @Description TODO
 * @Author su qinglin
 * @Date 2021/1/18 22:36
 * @Version 1.0
 **/
public class RxAddCard {

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
