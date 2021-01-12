package com.vians.admin.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @ClassName UserDetailInfo
 * @Description TODO
 * @Author su qinglin
 * @Date 2021/1/12 14:13
 * @Version 1.0
 **/
public class UserDetailInfo {

    @Getter
    @Setter
    private long id;

    @Getter
    @Setter
    private String userName;

    @Getter
    @Setter
    private String password;

    @Getter
    @Setter
    private int gender;

    @Getter
    @Setter
    private String cardId;

    @Getter
    @Setter
    private String phone;

    @Getter
    @Setter
    private long roleId;

    @Getter
    @Setter
    private String roleName;

    @Getter
    @Setter
    private String organization;

    @Getter
    @Setter
    private String department;

    @Getter
    @Setter
    private String workNumber;

    @Getter
    @Setter
    private String duty;

    @Getter
    @Setter
    private long projectId;

    @Getter
    @Setter
    private String projectName;

    @Getter
    @Setter
    private Date createTime;
}
