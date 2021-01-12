package com.vians.admin.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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

    public UserInfo() {
    }

    public UserInfo(long id, String phone, String password, String userName) {
        this.id = id;
        this.phone = phone;
        this.password = password;
        this.userName = userName;
    }
}
