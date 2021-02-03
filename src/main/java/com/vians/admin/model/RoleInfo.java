package com.vians.admin.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

/**
 * @ClassName RoleInfo
 * @Description TODO
 * @Author su qinglin
 * @Date 2021/1/12 15:11
 * @Version 1.0
 **/
public class RoleInfo {

    @Getter
    @Setter
    private int id;

    @Getter
    @Setter
    private String roleName;

    @Getter
    @Setter
    private Date createTime;

    @Getter
    @Setter
    private int[] checkedPermissionIds;

    @Getter
    @Setter
    private List<Permission> permissionList;
}
