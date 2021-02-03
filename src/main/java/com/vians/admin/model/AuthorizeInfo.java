package com.vians.admin.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @ClassName AuthorizeInfo
 * @Description TODO
 * @Author su qinglin
 * @Date 2021/1/18 11:18
 * @Version 1.0
 **/
public class AuthorizeInfo {

    @Getter
    @Setter
    private long id;

    @Getter
    @Setter
    private String content;

    @Getter
    @Setter
    private int type;

    @Getter
    @Setter
    private int position;

    @Getter
    @Setter
    private int timeType;

    @Getter
    @Setter
    private Date startTime;

    @Getter
    @Setter
    private Date endTime;
}
