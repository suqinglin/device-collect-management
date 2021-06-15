package com.vians.admin.model;

import lombok.Getter;

/**
 * 用于解析主题并生成对象
 * @ClassName MqttTopicInfo
 * @Description TODO
 * @Author su qinglin
 * @Date 2021/6/2 16:35
 * @Version 1.0
 **/
public class MqttTopicInfo {

    // Vians/v3/Req/SmartHome/DC2C26123456/Myhome/KT/1/LIGHT/0/3
    @Getter
    private String company; // 公司，如Vians

    @Getter
    private String ver; // 版本，如v3

    @Getter
    private String dir; // 方向，如Req

    @Getter
    private String action; // 动作，如SmartHome

    @Getter
    private String mac; // MAC地址，如DC2C26123456

    @Getter
    private String site; // 处所，如Myhome

    @Getter
    private String position; // 位置，如KT

    @Getter
    private int posIdx; // 位号，如1

    @Getter
    private String feature; // 特性号，如LIGHT

    @Getter
    private int type; // 类型，如0

    @Getter
    private int index; // 序号，如3

    public MqttTopicInfo(String topic) {
        // Vians/v3/Req/SmartHome/DC2C26123456/Myhome/KT/1/LIGHT/0/3
        String[] topicArr = topic.split("/");
        company = topicArr[0];
        ver = topicArr[1];
        dir = topicArr[2];
        action = topicArr[3];
        mac = topicArr[4];
        site = topicArr[5];
        position = topicArr[6];
        posIdx = Integer.parseInt(topicArr[7]);
        feature = topicArr[8];
        type = Integer.parseInt(topicArr[9]);
        index = Integer.parseInt(topicArr[10]);
    }
}
