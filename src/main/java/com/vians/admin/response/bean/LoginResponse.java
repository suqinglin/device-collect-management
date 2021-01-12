package com.vians.admin.response.bean;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
public class LoginResponse extends BResponse {

    @Getter
    @Setter
    private int UserID;

    @Getter
    @Setter
    private String CNT;
}
