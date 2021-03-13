package com.vians.admin.request;

import lombok.Getter;
import lombok.Setter;

/**
 * @ClassName RxCreatePermission
 * @Description TODO
 * @Author su qinglin
 * @Date 2021/3/12 17:35
 * @Version 1.0
 **/
public class RxCreatePermission {

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private String label;

    @Getter
    @Setter
    private long parentId;
}
