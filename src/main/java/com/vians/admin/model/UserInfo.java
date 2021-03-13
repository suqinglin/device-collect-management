package com.vians.admin.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.util.List;

/**
 * 用户信息
 */
@ToString
public class UserInfo {

    @Getter
    @Setter
    private long id;

    @Getter
    @Setter
    private String phone;

    @Getter
    @Setter
    private String password;

    @Getter
    @Setter
    private String userName;

    @Getter
    @Setter
    private int gender;

    @Getter
    @Setter
    private List<String> permissions;

    @Getter
    @Setter
    private long rootId;

    @Getter
    @Setter
    private String roleName;

    @Getter
    @Setter
    private long projectId;

    @Getter
    @Setter
    private String projectName;

    @Getter
    @Setter
    private String projectLogo;

    @Getter
    @Setter
    private Date createTime;

    public UserInfo() {
    }

    public UserInfo(long id, String phone, String password, long rootId, String userName) {
        this.id = id;
        this.phone = phone;
        this.password = password;
        this.rootId = rootId;
        this.userName = userName;
    }
}
