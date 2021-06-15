package com.vians.admin.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 设备电量实体
 * @ClassName DeviceBatt
 * @Description TODO
 * @Author su qinglin
 * @Date 2021/4/14 16:05
 * @Version 1.0
 **/
public class DeviceBatt {

    @Getter
    @Setter
    private long id;

    @Getter
    @Setter
    private String mac;

    @Getter
    @Setter
    private int batt;

    @Getter
    @Setter
    private Date updateTime;
}
