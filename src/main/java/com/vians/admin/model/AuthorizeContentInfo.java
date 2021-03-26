package com.vians.admin.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @ClassName AuthorizeContentInfo
 * @Description TODO
 * @Author su qinglin
 * @Date 2021/3/19 18:15
 * @Version 1.0
 **/
@NoArgsConstructor
public class AuthorizeContentInfo {

    public AuthorizeContentInfo(String content, int type) {
        this.content = content;
        this.type = type;
    }

    @Getter
    @Setter
    private long id;

    @Getter
    @Setter
    private String content;

    @Getter
    @Setter
    private int type;
}
