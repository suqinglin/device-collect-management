package com.vians.admin.response.bean;

import lombok.Getter;
import lombok.Setter;

/**
 * 获取获取锁具信息响应参数体
 */
public class GetLockInfoResponse extends BResponse {

    @Getter
    @Setter
    private String BlockIdx;

    @Getter
    @Setter
    private String CNT;

    @Getter
    @Setter
    private String GwMAC;

    @Getter
    @Setter
    private String GwUUID;

    @Getter
    @Setter
    private String LockIdx;
}
