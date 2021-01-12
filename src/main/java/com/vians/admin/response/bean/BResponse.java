package com.vians.admin.response.bean;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Response的父类，用于提取子response的公共属性
 */
@ToString
public class BResponse {

    @Getter
    @Setter
    private String Result;
}
