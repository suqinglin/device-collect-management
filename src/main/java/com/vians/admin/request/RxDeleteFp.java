package com.vians.admin.request;

import lombok.Getter;
import lombok.Setter;

/**
 * @ClassName RxDeleteFp
 * @Description TODO
 * @Author su qinglin
 * @Date 2021/1/19 18:12
 * @Version 1.0
 **/
public class RxDeleteFp {

    @Getter
    @Setter
    private long id;

    @Getter
    @Setter
    private long roomId;

    @Getter
    @Setter
    private String fpIdx;
}
