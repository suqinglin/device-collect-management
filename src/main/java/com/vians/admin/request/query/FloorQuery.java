package com.vians.admin.request.query;

import com.vians.admin.request.RxPage;
import lombok.Getter;
import lombok.Setter;

/**
 * @ClassName FloorQuery
 * @Description TODO
 * @Author su qinglin
 * @Date 2021/1/9 21:13
 * @Version 1.0
 **/
public class FloorQuery extends RxPage {

    @Getter
    @Setter
    private String floorName;

    @Getter
    @Setter
    private long unitId;
}
