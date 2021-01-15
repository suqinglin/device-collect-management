package com.vians.admin.request.query;

import com.vians.admin.request.RxPage;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @ClassName DeviceQuery
 * @Description TODO
 * @Author su qinglin
 * @Date 2021/1/15 14:24
 * @Version 1.0
 **/
@ToString
public class DeviceQuery extends RxPage {

    @Getter
    @Setter
    private long projectId;

    @Getter
    @Setter
    private long communityId;

    @Getter
    @Setter
    private long buildingId;

    @Getter
    @Setter
    private long unitId;

    @Getter
    @Setter
    private long floorId;

    @Getter
    @Setter
    private long roomId;

    @Getter
    @Setter
    private String deviceModel;

    @Getter
    @Setter
    private String deviceName;

    @Getter
    @Setter
    private String deviceFeature;

    @Getter
    @Setter
    private String deviceMac;

    @Getter
    @Setter
    private String gwMac;
}
