package com.vians.admin.request;

import lombok.Getter;
import lombok.Setter;

/**
 * @ClassName RxSelectCollectDevice
 * @Description TODO
 * @Author su qinglin
 * @Date 2021/3/8 17:51
 * @Version 1.0
 **/
public class RxSelectCollectDevice {

    @Getter
    @Setter
    private String browserUUID;

    @Getter
    @Setter
    private String mac;
}
