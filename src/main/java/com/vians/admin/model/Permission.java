package com.vians.admin.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;

/**
 * @ClassName Permission
 * @Description TODO
 * @Author su qinglin
 * @Date 2021/1/12 15:12
 * @Version 1.0
 **/
@ToString
public class Permission {

    @Getter
    @Setter
    private int id;

    @Getter
    @Setter
    private int parentId;

    @Getter
    @Setter
    private String label;

    @Getter
    @Setter
    private String pLabel;

    @Getter
    @Setter
    private boolean checked;

    @Getter
    @Setter
    private ArrayList<Permission> children;
}
