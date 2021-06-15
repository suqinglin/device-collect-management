package com.vians.admin.request;

import lombok.Getter;
import lombok.Setter;

/**
 * @ClassName RxStatisticsLog
 * @Description TODO
 * @Author su qinglin
 * @Date 2021/4/12 18:57
 * @Version 1.0
 **/
public class RxStatisticsLog {

    @Getter
    @Setter
    private String timeType;

    @Getter
    @Setter
    private long startTime;

    @Getter
    @Setter
    private long endTime;
}
