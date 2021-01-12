package com.vians.admin.response.bean;

import com.vians.admin.model.Battery;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 查询设备电量响应参数体
 */
public class CheckBattResponse extends BResponse {

    @Getter
    @Setter
    private String CNT;

    @Getter
    @Setter
    private List<Battery> Batt;

    @Getter
    @Setter
    private int Total;
}
