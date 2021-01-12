package com.vians.admin.request.query;

import com.vians.admin.request.RxPage;
import lombok.Getter;
import lombok.Setter;

/**
 * @ClassName BuildingQuery
 * @Description TODO
 * @Author su qinglin
 * @Date 2021/1/8 22:18
 * @Version 1.0
 **/
public class BuildingQuery extends RxPage {

    @Getter
    @Setter
    private String buildingName;

    @Getter
    @Setter
    private long communityId;
}
