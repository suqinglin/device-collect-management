package com.vians.admin.request;

import lombok.Getter;
import lombok.Setter;

/**
 * @ClassName RxFloor
 * @Description TODO
 * @Author su qinglin
 * @Date 2021/1/8 10:20
 * @Version 1.0
 **/
public class RxFloor {

    @Getter
    @Setter
    private long id;

    @Getter
    @Setter
    private String floorName;

    @Getter
    @Setter
    private long natureId;

    @Getter
    @Setter
    private long unitId;
}
