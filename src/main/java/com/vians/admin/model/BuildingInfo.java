package com.vians.admin.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

/**
 * @ClassName BuildingInfo
 * @Description TODO
 * @Author su qinglin
 * @Date 2021/1/8 9:47
 * @Version 1.0
 **/
public class BuildingInfo {

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
    private String natureName;

    @Setter
    @Getter
    private long communityId;

    @Getter
    @Setter
    private String communityName;

    @Getter
    @Setter
    private int unitCount;

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
    private List<UnitInfo> units;
}
