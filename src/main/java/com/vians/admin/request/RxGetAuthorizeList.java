package com.vians.admin.request;

import lombok.Getter;
import lombok.Setter;

/**
 * @ClassName RxGetAuthorizeList
 * @Description TODO
 * @Author su qinglin
 * @Date 2021/1/18 17:55
 * @Version 1.0
 **/
public class RxGetAuthorizeList {

    @Getter
    @Setter
    private long userId;

    @Getter
    @Setter
    private long roomId;
}
