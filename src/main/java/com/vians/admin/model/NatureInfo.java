package com.vians.admin.model;

import lombok.Getter;
import lombok.Setter;

/**
 * @ClassName Nature
 * @Description 性质实体(项目性质、小区性质、楼栋性质、单元性质、楼层性质、房号性质公用此类)
 * @Author su qinglin
 * @Date 2021/1/7 16:44
 * @Version 1.0
 **/
public class NatureInfo {

    @Getter
    @Setter
    private long id;

    @Getter
    @Setter
    private String natureName;
}
