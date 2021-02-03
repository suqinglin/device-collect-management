package com.vians.admin.request;

import lombok.Getter;
import lombok.Setter;

/**
 * @ClassName RxSaveRolePermissions
 * @Description TODO
 * @Author su qinglin
 * @Date 2021/1/28 17:31
 * @Version 1.0
 **/
public class RxSaveRolePermissions {

    @Getter
    @Setter
    private long roleId;

    @Getter
    @Setter
    private int[] permissionIds;
}
