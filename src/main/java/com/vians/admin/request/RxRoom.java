package com.vians.admin.request;

import lombok.Getter;
import lombok.Setter;

/**
 * @ClassName RxRoom
 * @Description TODO
 * @Author su qinglin
 * @Date 2021/1/8 11:36
 * @Version 1.0
 **/
public class RxRoom {

    @Getter
    @Setter
    private long id;

    @Getter
    @Setter
    private String roomName;

    @Getter
    @Setter
    private long natureId;

    @Getter
    @Setter
    private long floorId;

    @Getter
    @Setter
    private long roomModelId;

    @Getter
    @Setter
    private float area;

    @Getter
    @Setter
    private int state;
}
