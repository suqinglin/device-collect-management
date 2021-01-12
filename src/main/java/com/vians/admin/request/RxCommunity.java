package com.vians.admin.request;

import lombok.Getter;
import lombok.Setter;

/**
 * @ClassName RxCommunity
 * @Description TODO
 * @Author su qinglin
 * @Date 2021/1/8 9:18
 * @Version 1.0
 **/
public class RxCommunity {
    @Getter
    @Setter
    private long id;

    @Getter
    @Setter
    private String communityName;

    @Getter
    @Setter
    private long natureId;

    @Getter
    @Setter
    private long projectId;
}
