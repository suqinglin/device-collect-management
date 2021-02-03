package com.vians.admin.request.query;

import com.vians.admin.request.RxPage;
import lombok.Getter;
import lombok.Setter;

/**
 * @ClassName UserQuery
 * @Description TODO
 * @Author su qinglin
 * @Date 2021/1/12 15:09
 * @Version 1.0
 **/
public class UserQuery extends RxPage {

    @Getter
    @Setter
    private long roomId;

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private int gender;

    @Getter
    @Setter
    private String cardId;

    @Getter
    @Setter
    private String phone;

    @Getter
    @Setter
    private long roleId;

    @Getter
    @Setter
    private long projectId;
}
