package com.vians.admin.excel;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @ClassName ExcelUser
 * @Description TODO
 * @Author su qinglin
 * @Date 2021/1/26 14:34
 * @Version 1.0
 **/
@Builder
@ToString
public class ExcelUser {

    @Getter
    @Setter
    private String userName;

    @Getter
    @Setter
    private String password;

    @Getter
    @Setter
    private String gender;

    @Getter
    @Setter
    private String cardId;

    @Getter
    @Setter
    private String phone;

    @Getter
    @Setter
    private String role;

    @Getter
    @Setter
    private String organization;

    @Getter
    @Setter
    private String department;

    @Getter
    @Setter
    private String workNum;

    @Getter
    @Setter
    private String duty;
}
