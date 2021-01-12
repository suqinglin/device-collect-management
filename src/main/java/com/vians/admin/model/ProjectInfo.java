package com.vians.admin.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * @ClassName ProjectNature
 * @Description 项目实体
 * @Author su qinglin
 * @Date 2021/1/7 16:44
 * @Version 1.0
 **/
@ToString
public class ProjectInfo {

    @Getter
    @Setter
    private long id;

    @Getter
    @Setter
    private String projectName;

    @Getter
    @Setter
    private String address;

    @Getter
    @Setter
    private long natureId;

    @Getter
    @Setter
    private String natureName;

    @Getter
    @Setter
    private int communityCount;

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
}
