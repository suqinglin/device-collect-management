package com.vians.admin.response.bean;

import com.vians.admin.model.AlarmLog;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 查询告警日志响应参数体
 */
public class CheckAlarmResponse extends BResponse {

    @Getter
    @Setter
    private String CNT;

    @Getter
    @Setter
    private List<AlarmLog> Log;

    @Getter
    @Setter
    private int Total;
}