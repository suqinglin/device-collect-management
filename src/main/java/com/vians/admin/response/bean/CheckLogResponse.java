package com.vians.admin.response.bean;

import com.vians.admin.model.UnlockLog;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 查询开锁日志响应参数体
 */
public class CheckLogResponse extends BResponse {

    @Getter
    @Setter
    private String CNT;

    @Getter
    @Setter
    private List<UnlockLog> Log;

    @Getter
    @Setter
    private int Total;
}
