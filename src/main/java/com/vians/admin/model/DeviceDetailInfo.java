package com.vians.admin.model;

import lombok.Getter;
import lombok.Setter;

/**
 * 设备信息表
 */
public class DeviceDetailInfo extends DeviceBaseInfo {


    @Getter
    @Setter
    private String projectName;

    @Getter
    @Setter
    private String communityName;

    @Getter
    @Setter
    private String buildingName;

    @Getter
    @Setter
    private String unitName;

    @Getter
    @Setter
    private String floorName;

    @Getter
    @Setter
    private String roomName;

    @Getter
    @Setter
    private long roomId;


}
