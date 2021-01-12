package com.vians.admin.request;

import lombok.Getter;
import lombok.Setter;

/**
 * @ClassName RxPageLIst
 * @Description TODO
 * @Author su qinglin
 * @Date 2021/1/7 21:04
 * @Version 1.0
 **/
public class RxPage {

    @Getter
    @Setter
    private int pageIndex = 1;

    @Getter
    @Setter
    private int pageSize = 30;
}
