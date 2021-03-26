package com.vians.admin.request;

import lombok.Getter;
import lombok.Setter;

/**
 * @ClassName RxBindDevice
 * @Description TODO
 * @Author su qinglin
 * @Date 2021/1/15 16:32
 * @Version 1.0
 **/
public class RxBindDevice {

    @Getter
    @Setter
    private long deviceId;

    @Getter
    @Setter
    private long roomId;

    @Getter
    @Setter
    private int isDefault;
}
