package com.vians.admin.controller;

import com.alibaba.fastjson.JSONObject;
import com.vians.admin.common.CommConstants;
import com.vians.admin.excel.ExcelPOIHelper;
import com.vians.admin.model.DeviceBaseInfo;
import com.vians.admin.model.DeviceDetailInfo;
import com.vians.admin.model.RootUserInfo;
import com.vians.admin.request.RxBindDevice;
import com.vians.admin.request.RxId;
import com.vians.admin.request.RxPage;
import com.vians.admin.request.RxSelectCollectDevice;
import com.vians.admin.request.query.DeviceQuery;
import com.vians.admin.response.Page;
import com.vians.admin.response.ResponseCode;
import com.vians.admin.response.ResponseData;
import com.vians.admin.response.bean.GetDeviceResponse;
import com.vians.admin.service.DeviceService;
import com.vians.admin.service.RedisService;
import com.vians.admin.service.RootUserService;
import com.vians.admin.utils.HttpUtil;
import com.vians.admin.utils.KeyUtil;
import com.vians.admin.web.AppBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;

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

    final Logger logger = LoggerFactory.getLogger(getClass());

    @Resource
    public RedisService redisService;

    @Resource
    public DeviceService deviceService;

    @Resource
    public AppBean appBean;

    @Resource
    public RootUserService rootUserService;

    @PostMapping("/synch")
    public ResponseData synchDevice() {
        Long rootId = appBean.getRootUserId();
        Long projectId = appBean.getProjectId();
        if (projectId == null || rootId == null) {
            return ResponseData.error(ResponseCode.ERROR_USER_IS_ILLEGAL);
        }
        return doSyncDevices(rootId, projectId);
    }

    private ResponseData doSyncDevices(long rootId, long projectId) {
        RootUserInfo rootUserInfo = rootUserService.getRootUserById(rootId);
        if (rootUserInfo == null) {
            return ResponseData.error(ResponseCode.ERROR_USER_IS_ILLEGAL);
        }
        String userId = String.valueOf(rootUserInfo.getUserId());
        // cnt加1
        long nextCnt = rootUserInfo.getCnt() + 1;
        String appKey =  KeyUtil.getAppKey(rootUserInfo.getToken(), rootUserInfo.getPassword(), nextCnt);
        // 1. 组装请求参数
        Map<String, Object> requestParam = new HashMap<>();
        requestParam.put("UserID", userId); // 后台分配的用户号
        requestParam.put("Key", appKey); // 应用密钥 AppKey
        requestParam.put("MAC", ""); // MAC 地址
        requestParam.put("Feature", "");
        requestParam.put("Page", "1");
        requestParam.put("PageSize", "1000");
        // 2. 发送请求
        JSONObject respObj = HttpUtil.getInstance().doRequest(requestParam, CommConstants.C_API_URL + "/api/GetDevice");
        if (respObj != null) {
            int result = (int) respObj.get("Result");
            if (result == 0) {
                // 3. 解析出响应数据
                GetDeviceResponse getDeviceResponse = respObj.toJavaObject(GetDeviceResponse.class);
                rootUserInfo.setCnt(Long.parseLong(getDeviceResponse.getCNT(), 16));
                rootUserService.updateRootUser(rootUserInfo);
                List<DeviceBaseInfo> deviceBaseInfos = getDeviceResponse.getDev();
                List<String> dbMacs = deviceService.getDeviceMacs(projectId);
                if (deviceBaseInfos == null) {
                    // 如果同步到设备为空，则删除掉数据库中本项目下的所有设备
                    dbMacs.forEach(dbMac -> deviceService.deleteDeviceByMac(dbMac));
                } else {
                    // 本次同步的设备mac列表
                    List<String> syncMacs = new ArrayList<>();
                    // 4、保存到数据库
                    for (DeviceBaseInfo deviceBaseInfo : deviceBaseInfos) {
                        deviceBaseInfo.setUserId(userId);
                        deviceBaseInfo.setCreateTime(new Date());
                        deviceBaseInfo.setProjectId(projectId);
                        deviceService.addDevice(deviceBaseInfo);
                        if (!syncMacs.contains(deviceBaseInfo.getMAC())) {
                            syncMacs.add(deviceBaseInfo.getMAC());
                        }
                    }
                    // 删除掉项目下的同步中不包含的设备
                    dbMacs.forEach(dbMac -> {
                        if (!syncMacs.contains(dbMac)) {
                            deviceService.deleteDeviceByMac(dbMac);
                        }
                    });
                }
                return new ResponseData(ResponseCode.SUCCESS);
            } else if (result == 1002) {
                // 无效用户：重新登录
                return HttpUtil.getInstance().reLogin(rootUserInfo.getPhone(), rootUserInfo.getPassword(),
                        rootUserService, new HttpUtil.ReLoginCallback() {
                    @Override
                    public ResponseData success() {
                        // 登陆成功，重新同步设备
                        return doSyncDevices(rootId, projectId);
                    }

                    @Override
                    public ResponseData failure(int result) {
                        // 登陆失败，返回异常
                        return HttpUtil.getInstance().doResult(result);
                    }
                });
            } else {
                return HttpUtil.getInstance().doResult(result);
            }
        } else {
            return new ResponseData(ResponseCode.ERROR_DEVICE_GET_DEVICE_FAILURE);
        }
    }

    @PostMapping("/list")
    public ResponseData getDevicesByPage(@RequestBody DeviceQuery query) {

        Long projectId = appBean.getProjectId();
        if (projectId == null) {
            return ResponseData.error(ResponseCode.ERROR_USER_IS_ILLEGAL);
        }
        query.setProjectId(projectId);
        Page<DeviceDetailInfo> devicePage = deviceService.getDevicesByPage(query);
        ResponseData responseData = new ResponseData(ResponseCode.SUCCESS);
        responseData.addData("list", devicePage.getList());
        responseData.addData("total", devicePage.getTotal());
        return responseData;
    }

    /**
     * 获取采集设备，即指纹读卡器设备
     * @return
     */
    @PostMapping("/collectDevices")
    public ResponseData getCollectDevices() {
        Long rootId = appBean.getRootUserId();
        if (rootId == null) {
            return ResponseData.error(ResponseCode.ERROR_USER_IS_ILLEGAL);
        }
        ResponseData responseData = new ResponseData(ResponseCode.SUCCESS);
        List<DeviceBaseInfo> collectDevices = deviceService.getCollectDevicesByUser(rootId);
        responseData.addData("collectDevices", collectDevices);
        return responseData;
    }

    /**
     * 选择指纹读卡器设备
     * @return
     */
    @PostMapping("/selectCollectDevice")
    public ResponseData selectCollectDevice(@RequestBody RxSelectCollectDevice selectCollectDevice) {

        Long userId = appBean.getCurrentUserId();
        if (userId == null) {
            return new ResponseData(ResponseCode.ERROR_UN_AUTHORIZE_LOGIN);
        }
        deviceService.addBrowserDevice(selectCollectDevice.getBrowserUUID(), selectCollectDevice.getMac(), userId);
        return ResponseData.success();
    }

    @PostMapping("/deviceInRoom")
    public ResponseData getDeviceByRoomId(@RequestBody RxId rxId) {
        DeviceBaseInfo device = deviceService.getDeviceByRoomId(rxId.getId());
        ResponseData responseData = new ResponseData(ResponseCode.SUCCESS);
        responseData.addData("device", device);
        return responseData;
    }

    /**
     * 获取空闲设备
     * @return
     */
    @PostMapping("/freeDevices")
    public ResponseData getFreeDevices(@RequestBody RxPage page) {
        Long projectId = appBean.getProjectId();
        if (projectId == null) {
            return ResponseData.error(ResponseCode.ERROR_USER_IS_ILLEGAL);
        }
        Page<DeviceBaseInfo> devicePage = deviceService.getFreeDevicesByPage(page, projectId);
        ResponseData responseData = new ResponseData(ResponseCode.SUCCESS);
        responseData.addData("list", devicePage.getList());
        responseData.addData("total", devicePage.getTotal());
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
        deviceService.deleteDeviceById(id.getId());
        return new ResponseData(ResponseCode.SUCCESS);
    }

    @PostMapping("/uploadDevicesExcel")
    public ResponseData uploadDevicesExcel(@RequestParam(value = "file") MultipartFile file) {
        logger.info("Filename {}", file.getOriginalFilename());
        String fileName = file.getOriginalFilename();
        Long userId = appBean.getCurrentUserId();
        Long projectId = appBean.getProjectId();
        if (fileName == null || (!fileName.endsWith(".xls") && !fileName.endsWith(".xlsx"))) {
            return ResponseData.error();
        }
        if (userId == null) {
            return new ResponseData(ResponseCode.ERROR_ACCOUNT_NOT_LOGIN);
        }
        try {
            List<DeviceBaseInfo> deviceList = new ExcelPOIHelper().readDevicesExcel(fileName, file.getInputStream());
            logger.info("deviceList = {}", deviceList.toString());
            deviceService.batchAddDevices(deviceList, userId, projectId);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseData.success();
    }
}
