package com.vians.admin.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

/**
 * @ClassName CommunityInfo
 * @Description TODO
 * @Author su qinglin
 * @Date 2021/1/8 9:08
 * @Version 1.0
 **/
public class CommunityInfo {

    @Getter
    @Setter
    private long id;

    @Getter
    @Setter
    private String communityName;

    @Getter
    @Setter
    private long natureId;

    @Getter
    @Setter
    private String natureName;

    @Setter
    @Getter
    private long projectId;

    @Getter
    @Setter
    private String projectName;

    @Getter
    @Setter
    private int buildingCount;

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
    private List<BuildingInfo> buildings;
}
