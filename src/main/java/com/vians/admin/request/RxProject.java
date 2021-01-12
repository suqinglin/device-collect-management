package com.vians.admin.request;

import lombok.Getter;
import lombok.Setter;

/**
 * @ClassName RxAddProject
 * @Description TODO
 * @Author su qinglin
 * @Date 2021/1/7 20:41
 * @Version 1.0
 **/
public class RxProject {

    // id用于修改项目时定位到需要修改的项目
    @Getter
    @Setter
    private long id;

    @Getter
    @Setter
    private String projectName;

    @Getter
    @Setter
    private String address;

    @Getter
    @Setter
    private long natureId;
}
