package com.vians.admin.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @ClassName DeviceBaseInfo
 * @Description TODO
 * @Author su qinglin
 * @Date 2021/1/15 15:26
 * @Version 1.0
 **/
public class DeviceBaseInfo {

    @Getter
    @Setter
    private long id;

    @Getter
    @Setter
    private String MAC;

    @Getter
    @Setter
    private String GwMac;

    /**
     * 设备型号
     */
    @Getter
    @Setter
    private String Device;

    @Getter
    @Setter
    private String Active;

    @Getter
    @Setter
    private String Feature;

    @Getter
    @Setter
    private String Index;

    /**
     * 由于index为mysql关键字，查询不能用，故简写为idx只为查询用
     */
    @Getter
    @Setter
    private String idx;

    @Getter
    @Setter
    private String Name;

    @Getter
    @Setter
    private int ele;

    @Getter
    @Setter
    private String bindUserName;

    @Getter
    @Setter
    private String createUserName;

    @Getter
    @Setter
    private Date createTime;

    @Getter
    @Setter
    private Date bindTime;

    @Getter
    @Setter
    private String userId;

    @Getter
    @Setter
    private long bindUserId;
}
