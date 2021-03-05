package com.vians.admin.request;

import lombok.Getter;
import lombok.Setter;

/**
 * @ClassName RxBuilding
 * @Description TODO
 * @Author su qinglin
 * @Date 2021/1/8 9:46
 * @Version 1.0
 **/
public class RxBuilding {

    @Getter
    @Setter
    private long id;

    @Getter
    @Setter
    private String buildingName;

    @Getter
    @Setter
    private long natureId;

    @Getter
    @Setter
    private long communityId;

    @Getter
    @Setter
    private int unitCount;
}
