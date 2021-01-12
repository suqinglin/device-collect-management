package com.vians.admin.request.query;

import com.vians.admin.request.RxPage;
import lombok.Getter;
import lombok.Setter;

/**
 * @ClassName ProjectQuery
 * @Description TODO
 * @Author su qinglin
 * @Date 2021/1/8 22:18
 * @Version 1.0
 **/
public class ProjectQuery extends RxPage {

    @Getter
    @Setter
    private String projectName;
}
