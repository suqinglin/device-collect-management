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

    /**
     * 一般用户
     */
    public static final int USER_TYPE_GENERAL_USER = 0;
    /**
     * 项目管理员
     */
    public static final int USER_TYPE_PROJECT_MANAGER = 1;

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

    @Getter
    @Setter
    private int type;
}
