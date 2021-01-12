package com.vians.admin.request;

import lombok.Getter;
import lombok.Setter;

/**
 * 查询设备电量请求体
 */
public class RxCheckBatt {

    @Getter
    @Setter
    private String mac;

    @Getter
    @Setter
    private String feature;

    @Getter
    @Setter
    private String page;

    @Getter
    @Setter
    private String pageSize;

}
