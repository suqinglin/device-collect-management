package com.vians.admin.request;

import lombok.Getter;
import lombok.Setter;

/**
 * @ClassName RxUnit
 * @Description TODO
 * @Author su qinglin
 * @Date 2021/1/8 10:38
 * @Version 1.0
 **/
public class RxUnit {

    @Getter
    @Setter
    private long id;

    @Getter
    @Setter
    private String unitName;

    @Getter
    @Setter
    private long natureId;

    @Getter
    @Setter
    private long buildingId;
}
