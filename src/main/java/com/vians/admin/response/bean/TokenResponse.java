package com.vians.admin.response.bean;

import lombok.Getter;
import lombok.Setter;

/**
 * 获取token时的响应体结构
 */
public class TokenResponse extends BResponse {

    @Getter
    @Setter
    private String Token;
}
