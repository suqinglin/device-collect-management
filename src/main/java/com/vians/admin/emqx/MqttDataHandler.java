package com.vians.admin.emqx;

import com.vians.admin.common.DeviceHelper;
import com.vians.admin.model.MqttTopicInfo;
import com.vians.admin.service.DeviceService;
import com.vians.admin.service.RedisService;
import com.vians.admin.utils.CommUtil;
import com.vians.admin.utils.PayloadEnDeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

/**
 * @ClassName MqttDataHandle
 * @Description TODO
 * @Author su qinglin
 * @Date 2021/6/2 16:03
 * @Version 1.0
 **/

@Component
public class MqttDataHandler {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Resource
    private DeviceService deviceService;

    @Resource
    private RedisService redisService;

    @Resource
    private UploadLogHandler uploadLogHandler;

    public void handleData(String topic, String payload) {
        MqttTopicInfo topicInfo = new MqttTopicInfo(topic);
        String mac = topicInfo.getMac();
        String UUID = DeviceHelper.getUUID(deviceService, redisService, mac);
        if (StringUtils.isEmpty(UUID)) {
            logger.info("UUID未找到");
            return;
        }
        String result = PayloadEnDeUtil.decryptByUUID(UUID, payload);
        if (!PayloadEnDeUtil.checkCrc(result)) {
            logger.info("CRC校验错误");
            return;
        }
        logger.info("result:{}", result);
        byte[] data = CommUtil.hexStringToBytes(result);
        if (data[0] != data.length - 2) {
            logger.info("数据长度错误");
            return;
        }
//        MqttTopicFeature
        switch (topicInfo.getFeature()) {
            case "LOCK":
                if (data[1] == 0x05) { // 上传日志
                    uploadLogHandler.handleLockLogs(data);
                }
                break;
            case "GATEWAY":
                break;
            case "GEN_ALARM":
            case "DOOR_ALARM":
            case "GAS_ALARM":
            case "SMOKE_ALARM":
                if (data[1] == 0x02) { // 设备端主动上报日志，包括告警和电量
                    uploadLogHandler.handleOtherLogs(data, topicInfo.getFeature());
                }
                break;
        }
    }

}
