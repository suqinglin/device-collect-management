package com.vians.admin.request;

import lombok.Getter;
import lombok.Setter;

/**
 * 查询开锁日志请求体
 */
public class RxCheckLog {

    @Getter
    @Setter
    private String mac;

    @Getter
    @Setter
    private String index;

    @Getter
    @Setter
    private String beginTime;

    @Getter
    @Setter
    private String endTime;

    @Getter
    @Setter
    private String Page;

    @Getter
    @Setter
    private String PageSize;

}
