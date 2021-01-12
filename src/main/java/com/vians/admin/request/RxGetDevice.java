package com.vians.admin.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 获取该用户的设备列表请求参数体
 */
@ToString
public class RxGetDevice {

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
