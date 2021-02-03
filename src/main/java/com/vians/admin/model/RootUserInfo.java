package com.vians.admin.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * @ClassName RootUserInfo
 * @Description TODO
 * @Author su qinglin
 * @Date 2021/1/28 21:00
 * @Version 1.0
 **/
@ToString
public class RootUserInfo {

    @Getter
    @Setter
    private long userId;

    @Getter
    @Setter
    private String phone;

    @Getter
    @Setter
    private String password;

    @Getter
    @Setter
    private String token;

    @Getter
    @Setter
    private long cnt;

    @Getter
    @Setter
    private Date updateTime;
}
