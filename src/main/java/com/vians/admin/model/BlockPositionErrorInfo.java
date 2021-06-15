package com.vians.admin.model;

import lombok.Getter;
import lombok.Setter;

/**
 * 数据表xy_device_local_feature中部分Block和Lock位置错误
 * 这个实体类用于数据库查询错误记录返回ID
 * @ClassName BlockPositionErrorInfo
 * @Description TODO
 * @Author su qinglin
 * @Date 2021/4/7 15:06
 * @Version 1.0
 **/
public class BlockPositionErrorInfo {

    @Getter
    @Setter
    private long blockId;

    @Getter
    @Setter
    private long lockId;

    @Getter
    @Setter
    private String mac;
}
