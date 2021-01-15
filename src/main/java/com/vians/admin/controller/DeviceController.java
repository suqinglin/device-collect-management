package com.vians.admin.controller;

import com.alibaba.fastjson.JSONObject;
import com.vians.admin.common.RedisKeyConstants;
import com.vians.admin.model.DeviceBaseInfo;
import com.vians.admin.model.DeviceDetailInfo;
import com.vians.admin.request.RxBindDevice;
import com.vians.admin.request.RxId;
import com.vians.admin.request.query.DeviceQuery;
import com.vians.admin.response.Page;
import com.vians.admin.response.ResponseCode;
import com.vians.admin.response.ResponseData;
import com.vians.admin.response.bean.GetDeviceResponse;
import com.vians.admin.service.DeviceService;
import com.vians.admin.service.RedisService;
import com.vians.admin.utils.HttpUtil;
import com.vians.admin.utils.KeyUtil;
import com.vians.admin.utils.PropUtil;
import com.vians.admin.web.AppBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName DeviceController
 * @Description TODO
 * @Author su qinglin
 * @Date 2021/1/15 9:27
 * @Version 1.0
 **/

@CrossOrigin
@RestController
@RequestMapping("/vians/Device")
public class DeviceController {

    @Resource
    public RedisService redisService;

    @Autowired
    public PropUtil propUtil;

    @Resource
    public DeviceService deviceService;

    @Resource
    public AppBean appBean;

    @PostMapping("/synch")
    public ResponseData synchDevice() {

        String token = redisService.get(RedisKeyConstants.VIANS_TOKEN);
        String cnt = redisService.get(RedisKeyConstants.VIANS_CNT);
        String userId = redisService.get(RedisKeyConstants.VIANS_USER_ID);
        if (StringUtils.isEmpty(token)) {
            return new ResponseData(ResponseCode.ERROR_DEVICE_PHYSIC_NOT_GET_TOKEN);
        }
        if (StringUtils.isEmpty(cnt) || StringUtils.isEmpty(userId)) {
            return new ResponseData(ResponseCode.ERROR_DEVICE_PHYSIC_NOT_LOGIN);
        }
        // cnt加1
        int nextCnt = Integer.parseInt(cnt, 16) + 1;
        String appKey =  KeyUtil.getAppKey(token, propUtil.physicPsw, nextCnt);
        // 1. 组装请求参数
        Map<String, Object> requestParam = new HashMap<>();
        requestParam.put("UserID", userId); // 后台分配的用户号
        requestParam.put("Key", appKey); // 应用密钥 AppKey
        requestParam.put("MAC", ""); // MAC 地址
        requestParam.put("Feature", "");
        requestParam.put("Page", "1");
        requestParam.put("PageSize", "1000");
        // 2. 发送请求
        JSONObject respObj = HttpUtil.getInstance().doRequest(requestParam, propUtil.url + "/api/GetDevice");
        if (respObj != null) {
            int result = (int) respObj.get("Result");
            if (result == 0) {
                // 3. 解析出响应数据
                GetDeviceResponse getDeviceResponse = respObj.toJavaObject(GetDeviceResponse.class);
                redisService.set(RedisKeyConstants.VIANS_CNT, getDeviceResponse.getCNT());
                List<DeviceBaseInfo> deviceBaseInfos = getDeviceResponse.getDev();
                // 4、保存到数据库
                for (int i = 0; i < deviceBaseInfos.size(); i++) {
                    DeviceBaseInfo deviceBaseInfo = deviceBaseInfos.get(i);
                    deviceBaseInfo.setUserId(userId);
                    deviceBaseInfo.setCreateTime(new Date());
                    deviceService.addDevice(deviceBaseInfo);
                }
                return new ResponseData(ResponseCode.SUCCESS);
            } else {
                return HttpUtil.getInstance().doResult(result);
            }
        } else {
            return new ResponseData(ResponseCode.ERROR_DEVICE_GET_DEVICE_FAILURE);
        }
    }

    @PostMapping("/list")
    public ResponseData getDevicesByPage(@RequestBody DeviceQuery query) {

        Page<DeviceDetailInfo> devicePage = deviceService.getDevicesByPage(query);
        ResponseData responseData = new ResponseData(ResponseCode.SUCCESS);
        responseData.addData("list", devicePage.getList());
        responseData.addData("total", devicePage.getTotal());
        return responseData;
    }

    /**
     * 获取空闲设备
     * @return
     */
    @PostMapping("/freeDevices")
    public ResponseData getFreeDevices() {
        List<DeviceBaseInfo> devices = deviceService.getFreeDevices();
        ResponseData responseData = new ResponseData(ResponseCode.SUCCESS);
        responseData.addData("devices", devices);
        return responseData;
    }

    /**
     * 获取指定房间中绑定的设备
     * @return
     */
    @PostMapping("/bindedDevices")
    public ResponseData getBindedDevices(@RequestBody RxId rxId) {
        List<DeviceBaseInfo> devices = deviceService.getBindedDevices(rxId.getId());
        ResponseData responseData = new ResponseData(ResponseCode.SUCCESS);
        responseData.addData("devices", devices);
        return responseData;
    }

    /**
     * 将设备绑定到房间
     * @param bindDevice
     * @return
     */
    @PostMapping("/bind")
    public ResponseData bindDevice(@RequestBody RxBindDevice bindDevice) {
        Long userId = appBean.getCurrentUserId();
        if (userId == null) {
            return new ResponseData(ResponseCode.ERROR_UN_AUTHORIZE_LOGIN);
        }
        deviceService.bindDevice(bindDevice.getRoomId(), bindDevice.getDeviceId(), userId);
        return new ResponseData(ResponseCode.SUCCESS);
    }

    /**
     * 解绑设备
     * @param rxId
     * @return
     */
    @PostMapping("/unbind")
    public ResponseData unbindDevice(@RequestBody RxId rxId) {
        deviceService.unbindDevice(rxId.getId());
        return new ResponseData(ResponseCode.SUCCESS);
    }

    @PostMapping("/delete")
    public ResponseData deleteDevice(@RequestBody RxId id) {
        deviceService.deleteDevice(id.getId());
        return new ResponseData(ResponseCode.SUCCESS);
    }
}
