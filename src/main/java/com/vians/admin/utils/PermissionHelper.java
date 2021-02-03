package com.vians.admin.utils;

import com.vians.admin.mapper.ViansUserMapper;
import com.vians.admin.model.Permission;

import java.util.List;

/**
 * @ClassName PermissionHelper
 * @Description TODO
 * @Author su qinglin
 * @Date 2021/1/28 16:28
 * @Version 1.0
 **/
public class PermissionHelper {

    private static PermissionHelper permissionHelper;
    private static List<Permission> permissionList;
    private PermissionHelper() {

    }

    public static PermissionHelper getInstance(ViansUserMapper mapper) {
        if (permissionHelper == null) {
            permissionHelper = new PermissionHelper();
            permissionList = mapper.getPermissionTree();
        }
        return permissionHelper;
    }

    public List<Permission> getPermissionList() {
        return permissionList;
    }
}
