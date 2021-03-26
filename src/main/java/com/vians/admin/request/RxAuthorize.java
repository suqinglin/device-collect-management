package com.vians.admin.request;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @ClassName RxAuthorize
 * @Description TODO
 * @Author su qinglin
 * @Date 2021/1/18 18:08
 * @Version 1.0
 **/
public class RxAuthorize {

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
    private long userId;

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

    @Getter
    @Setter
    private Date createTime;

    @Getter
    @Setter
    private long createUserId;

    @Getter
    @Setter
    private String tempPath;

    @Getter
    @Setter
    private String imgPath;

    @Getter
    @Setter
    private long roomId;

    @Getter
    @Setter
    private int index;

    @Getter
    @Setter
    private long tempContentId;

    @Getter
    @Setter
    private long imgContentId;
}
