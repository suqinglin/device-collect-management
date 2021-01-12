package com.vians.admin.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 读取Properties系统配置参数
 */
@Component
public class PropUtil {

    /**
     * 第三方陈春服务器的url
     */
    @Value("${system.url}")
    public String url;

    /**
     * 物理账号：用来与陈春的c++服务器通信的唯一账号
     */
    @Value("${system.physicUser}")
    public String physicUser;

    /**
     * 物理账号对应的密码
     */
    @Value("${system.physicPsw}")
    public String physicPsw;

    /**
     * 应用名称
     */
    @Value("${spring.application.name}")
    public String applicationName;
}
