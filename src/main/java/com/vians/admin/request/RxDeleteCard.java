package com.vians.admin.request;

import lombok.Getter;
import lombok.Setter;

/**
 * @ClassName RxDeleteCard
 * @Description TODO
 * @Author su qinglin
 * @Date 2021/1/19 18:12
 * @Version 1.0
 **/
public class RxDeleteCard {

    @Getter
    @Setter
    private long id;

    @Getter
    @Setter
    private long roomId;

    @Getter
    @Setter
    private String cardNum;

    @Getter
    @Setter
    private String cardIdx;
}
