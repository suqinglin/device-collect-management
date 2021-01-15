package com.vians.admin.response.bean;

import com.vians.admin.model.DeviceBaseInfo;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 获取该用户的设备列表响应参数体
 */
public class GetDeviceResponse extends BResponse {

    @Getter
    @Setter
    private String CNT;

    @Getter
    @Setter
    private List<DeviceBaseInfo> Dev;

    @Getter
    @Setter
    private int Total;
}
