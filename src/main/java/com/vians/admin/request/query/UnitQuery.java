package com.vians.admin.request.query;

import com.vians.admin.request.RxPage;
import lombok.Getter;
import lombok.Setter;

/**
 * @ClassName UnitQuery
 * @Description TODO
 * @Author su qinglin
 * @Date 2021/1/9 21:12
 * @Version 1.0
 **/
public class UnitQuery extends RxPage {

    @Getter
    @Setter
    private String unitName;

    @Getter
    @Setter
    private long buildingId;
}
