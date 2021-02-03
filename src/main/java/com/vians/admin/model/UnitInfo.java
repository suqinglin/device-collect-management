package com.vians.admin.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

/**
 * @ClassName UnitInfo
 * @Description TODO
 * @Author su qinglin
 * @Date 2021/1/8 10:37
 * @Version 1.0
 **/
public class UnitInfo {

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
    private String natureName;

    @Setter
    @Getter
    private long buildingId;

    @Getter
    @Setter
    private String buildingName;

    @Getter
    @Setter
    private int floorCount;

    @Getter
    @Setter
    private long createUserId;

    @Getter
    @Setter
    private String createUserName;

    @Getter
    @Setter
    private Date createTime;

    @Getter
    @Setter
    private Date updateTime;

    @Getter
    @Setter
    private List<FloorInfo> floors;
}
