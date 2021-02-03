package com.vians.admin.request;

import lombok.Getter;
import lombok.Setter;

/**
 * @ClassName RxSaveRootUser
 * @Description TODO
 * @Author su qinglin
 * @Date 2021/1/29 15:32
 * @Version 1.0
 **/
public class RxSaveRootUser {

    @Getter
    @Setter
    private String rootUserPhone;

    @Getter
    @Setter
    private String password;

    @Getter
    @Setter
    private long projectId;
}
