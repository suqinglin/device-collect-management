package com.suql.devicecollect.model;

import lombok.Getter;
import lombok.Setter;

/**
 * 用户信息
 */
public class UserInfo {

    @Getter
    @Setter
    private long id;

    @Getter
    @Setter
    private String account;

    @Getter
    @Setter
    private String password;

    @Getter
    @Setter
    private String nickName;

    @Getter
    @Setter
    private long createTime;

    public UserInfo() {
    }

    public UserInfo(long id, String account, String password, String nickName, long createTime) {
        this.id = id;
        this.account = account;
        this.password = password;
        this.nickName = nickName;
        this.createTime = createTime;
    }
}
