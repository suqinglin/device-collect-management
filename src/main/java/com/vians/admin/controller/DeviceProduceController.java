package com.vians.admin.controller;

import com.vians.admin.common.RedisKeyConstants;
import com.vians.admin.model.DeviceInfo;
import com.vians.admin.request.RxDecryptPayload;
import com.vians.admin.request.RxEncryptPayload;
import com.vians.admin.response.ResponseCode;
import com.vians.admin.response.ResponseData;
import com.vians.admin.service.DeviceService;
import com.vians.admin.service.RedisService;
import com.vians.admin.utils.PayloadEnDeUtil;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@CrossOrigin
@RestController
@RequestMapping("/vians/deviceProduce")
public class DeviceProduceController {

    @Resource
    private DeviceService deviceService;

    @Resource
    private RedisService redisService;


    @PostMapping("/encryptPayload")
    public ResponseData encryptPayload(@RequestBody RxEncryptPayload payloadEncrypt) {
        String mac = payloadEncrypt.getMac();
        String UUID = getUUID(mac);
        if (StringUtils.isEmpty(UUID)) {
            return ResponseData.error(ResponseCode.ERROR_DEVICE_NOT_EXIST);
        }
        String result = PayloadEnDeUtil.encryptByUUID(UUID, payloadEncrypt.getReqHex());
        ResponseData responseData = new ResponseData(ResponseCode.SUCCESS);
        responseData.addData("enResult", result);
        return responseData;
    }

    @PostMapping("/decryptPayload")
    public ResponseData decryptPayload(@RequestBody RxDecryptPayload decryptPayload) {
        String mac = decryptPayload.getMac();
        String UUID = getUUID(mac);
        if (StringUtils.isEmpty(UUID)) {
            return ResponseData.error(ResponseCode.ERROR_DEVICE_NOT_EXIST);
        }
        String result = PayloadEnDeUtil.decryptByUUID(UUID, decryptPayload.getRespStr());
        System.out.println("decryptPayload->result:" + result);
        if (PayloadEnDeUtil.checkCrc(result)) {
            ResponseData responseData = new ResponseData(ResponseCode.SUCCESS);
            responseData.addData("enResult", result);
            return responseData;
        } else {
            return new ResponseData(ResponseCode.ERROR_DEVICE_CRC_ERROR);
        }
    }

    /**
     * 根据设备MAC获取UUID
     * @param mac
     * @return
     */
    private String getUUID(String mac) {
        String UUID = redisService.get(RedisKeyConstants.DEVICE_MAC_UUID + mac);
        if (StringUtils.isEmpty(UUID)) {
            DeviceInfo deviceInfo = deviceService.getDeviceInfoByMac(mac.contains("FFFF") ? mac : "FFFF" + mac);
            if (deviceInfo == null) {
                return null;
            }
            UUID = deviceInfo.getUuid();
            redisService.set(RedisKeyConstants.DEVICE_MAC_UUID + mac, UUID);
        }
        return UUID;
    }
}
