package com.vians.admin.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @ClassName RoomInfo
 * @Description TODO
 * @Author su qinglin
 * @Date 2021/1/8 11:25
 * @Version 1.0
 **/
public class RoomInfo {

    /**
     * 房间ID
     */
    @Getter
    @Setter
    private long id;

    /**
     * 房间名称
     */
    @Getter
    @Setter
    private String roomName;

    /**
     * 房间全称
     */
    @Getter
    @Setter
    private String roomFullName;

    /**
     * 房间性质ID
     */
    @Getter
    @Setter
    private long natureId;

    /**
     * 房间性质名称
     */
    @Getter
    @Setter
    private String natureName;

    /**
     * 所属楼层ID
     */
    @Setter
    @Getter
    private long floorId;

    /**
     * 所属楼层名称
     */
    @Getter
    @Setter
    private String floorName;

    /**
     * 创建人ID
     */
    @Getter
    @Setter
    private long createUserId;

    /**
     * 创建人名称
     */
    @Getter
    @Setter
    private String createUserName;

    /**
     * 创建时间
     */
    @Getter
    @Setter
    private Date createTime;

    /**
     * 最后更新时间
     */
    @Getter
    @Setter
    private Date updateTime;

    /**
     * 房间户型ID
     */
    @Getter
    @Setter
    private long roomModelId;

    /**
     * 房间户型名称
     */
    @Getter
    @Setter
    private String roomModelName;

    /**
     * 房间面积
     */
    @Getter
    @Setter
    private float area;

    /**
     * 房间状态
     */
    @Getter
    @Setter
    private int state;

    /**
     * 设备数
     */
    @Getter
    @Setter
    private int deviceCount;

    /**
     * 住客数
     */
    @Getter
    @Setter
    private int userCount;

    @Getter
    @Setter
    private String unitName;

    @Getter
    @Setter
    private String buildingName;

    @Getter
    @Setter
    private String communityName;

    @Getter
    @Setter
    private String projectName;

    @Getter
    @Setter
    private int passwordCount;

    @Getter
    @Setter
    private int fpCount;

    @Getter
    @Setter
    private int cardCount;

    @Getter
    @Setter
    private int faceCount;

    @Getter
    @Setter
    private int bluetoothCount;

    @Getter
    @Setter
    private String defaultDevice;

}
