package com.vians.admin.model;

import lombok.Getter;
import lombok.Setter;

/**
 * 开锁日志
 */
public class UnlockLog {

    @Getter
    @Setter
    private String Name;

    @Getter
    @Setter
    private String MAC;

    @Getter
    @Setter
    private int Time;

    @Getter
    @Setter
    private String Action;

    @Getter
    @Setter
    private String Value;

}
