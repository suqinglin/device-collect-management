package com.vians.admin.request;

import lombok.Getter;
import lombok.Setter;

/**
 * @ClassName RxModifyPsw
 * @Description TODO
 * @Author su qinglin
 * @Date 2021/1/28 20:00
 * @Version 1.0
 **/
public class RxModifyPsw {

    @Getter
    @Setter
    private String oldPassword;

    @Getter
    @Setter
    private String newPassword;
}
