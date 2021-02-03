package com.vians.admin.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @ClassName DataDirctionary
 * @Description TODO
 * @Author su qinglin
 * @Date 2021/1/22 18:18
 * @Version 1.0
 **/
public class DataDir {

    @Getter
    @Setter
    private long id;

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private List<DataDir> children;
}
