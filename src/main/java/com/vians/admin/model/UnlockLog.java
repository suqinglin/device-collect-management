package com.vians.admin.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 开锁日志
 */
@ToString
public class UnlockLog {

    @Getter
    @Setter
    private int id;

    @Getter
    @Setter
    private String cardId;

    @Getter
    @Setter
    private String roleName;

    @Getter
    @Setter
    private long projectId;

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

    /**
     * 用户名称
     */
    @Getter
    @Setter
    private String Name;

    @Getter
    @Setter
    private String MAC;

    @Getter
    @Setter
    private String Time;

    @Getter
    @Setter
    private String Action;

    @Getter
    @Setter
    private String Value;

    @Getter
    @Setter
    private String deviceName;

    @Getter
    @Setter
    private String deviceModel;

}
