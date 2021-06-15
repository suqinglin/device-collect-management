package com.vians.admin.request;

import lombok.Getter;
import lombok.Setter;

/**
 * @ClassName RxSychLogs
 * @Description TODO
 * @Author su qinglin
 * @Date 2021/6/5 14:37
 * @Version 1.0
 **/
public class RxSychLogs {

    @Getter
    @Setter
    private long projectId;

    @Getter
    @Setter
    private String startTime;

    @Getter
    @Setter
    private String endTime;
}
