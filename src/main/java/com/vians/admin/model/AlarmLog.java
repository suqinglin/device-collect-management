package com.vians.admin.model;

import lombok.Getter;
import lombok.Setter;

/**
 * 告警日志
 */
public class AlarmLog {

    @Getter
    @Setter
    private String MAC;

    @Getter
    @Setter
    private int Time;

    @Getter
    @Setter
    private String Action;

}
