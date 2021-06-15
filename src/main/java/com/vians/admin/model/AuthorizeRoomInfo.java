package com.vians.admin.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 授权和房间的关联表
 * @ClassName AuthorizeRoomInfo
 * @Description TODO
 * @Author su qinglin
 * @Date 2021/5/10 17:03
 * @Version 1.0
 **/
public class AuthorizeRoomInfo {

    @Getter
    @Setter
    private long id;

    @Getter
    @Setter
    private long roomId;

    @Getter
    @Setter
    private long authorizeId;

    @Getter
    @Setter
    private int authorizeType;

    @Getter
    @Setter
    private int position;

    @Getter
    @Setter
    private Date createTime;

    @Getter
    @Setter
    private Date updateTime;
}
