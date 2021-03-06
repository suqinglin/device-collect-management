package com.suql.devicecollect.controller;

import com.suql.devicecollect.model.DeviceInfo;
import com.suql.devicecollect.model.PrintDeviceBean;
import com.suql.devicecollect.print.PrintService;
import com.suql.devicecollect.request.*;
import com.suql.devicecollect.response.Page;
import com.suql.devicecollect.response.Pageable;
import com.suql.devicecollect.response.ResponseCode;
import com.suql.devicecollect.response.ResponseData;
import com.suql.devicecollect.service.DeviceDescribeService;
import com.suql.devicecollect.service.DeviceService;
import com.suql.devicecollect.service.UserService;
import com.suql.devicecollect.utils.CommUtil;
import com.suql.devicecollect.web.AppBean;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/device")
public class DeviceController {

    @Resource
    private AppBean appBean;

    @Resource
    private UserService userService;

    @Resource
    private DeviceService deviceService;

    @Resource
    private DeviceDescribeService deviceDescribeService;

    @Resource
    private PrintService printService;

    @PostMapping("/macAndToken")
    public ResponseData getMacAndToken(@Valid @RequestBody RxGetMacAndToken getMacAndToken) {

        String uuid = getMacAndToken.getUuid();
        Long userId = appBean.getCurrentUserId();
        if (userId == null) {
            return ResponseData.error(ResponseCode.ERROR_ACCOUNT_NOT_LOGIN);
        }

        DeviceInfo deviceInfo = deviceService.getDeviceInfoByUuid(uuid);
        String mac;
        String token;
        if (deviceInfo == null) {
            mac = deviceService.getNextMac();
            token = CommUtil.getRandom8Hex();
            deviceService.saveMacAndToken(userId, uuid, mac, token);
        } else {
            mac = deviceInfo.getMac();
            token = deviceInfo.getToken();
        }
        ResponseData responseData = new ResponseData(ResponseCode.SUCCESS);
        responseData.addData("mac", mac);
        responseData.addData("token", token);
        return responseData;
    }

    @PostMapping("/getDeviceToken")
    public ResponseData getDeviceToken(@Valid @RequestBody RxGetDeviceToken getDeviceToken) {
        Long userId = appBean.getCurrentUserId();
        if (userId == null) {
            return ResponseData.error(ResponseCode.ERROR_ACCOUNT_NOT_LOGIN);
        }
        String mac = getDeviceToken.getMac();
        DeviceInfo device = deviceService.getDeviceInfoByMac(mac);
        if (device == null) {
            return ResponseData.error(ResponseCode.ERROR_DEVICE_NOT_EXIST);
        }
        ResponseData responseData = new ResponseData(ResponseCode.SUCCESS);
        responseData.addData("token", device.getToken());
//        responseData.addData("uuid", device.getUuid());
        responseData.addData("model", device.getModel());
        return responseData;
    }

    @PostMapping("/uploadDeviceInfo")
    public ResponseData uploadDeviceInfo(@Valid @RequestBody RxDeviceInfo rxDeviceInfo) {

        Long userId = appBean.getCurrentUserId();
        if (userId == null) {
            return ResponseData.error(ResponseCode.ERROR_ACCOUNT_NOT_LOGIN);
        }
        DeviceInfo device = deviceService.getDeviceInfoByMac(rxDeviceInfo.getMac());
        if (device == null) {
            return ResponseData.error(ResponseCode.ERROR_DEVICE_NOT_EXIST);
        }
        if (!StringUtils.isEmpty(rxDeviceInfo.getUuid())) {
            device.setUuid(rxDeviceInfo.getUuid());
        }
        if (!StringUtils.isEmpty(rxDeviceInfo.getToken())) {
            device.setToken(rxDeviceInfo.getToken());
        }
        if (!StringUtils.isEmpty(rxDeviceInfo.getSn())) {
            device.setSnNum(Long.valueOf(rxDeviceInfo.getSn()));
        }
        if (!StringUtils.isEmpty(rxDeviceInfo.getModel())) {
            device.setModel(rxDeviceInfo.getModel());
        }
        if (!StringUtils.isEmpty(rxDeviceInfo.getHwVersion())) {
            device.setHwVersion(rxDeviceInfo.getHwVersion());
        }
        if (!StringUtils.isEmpty(rxDeviceInfo.getFwVersion())) {
            device.setFwVersion(rxDeviceInfo.getFwVersion());
        }
        if (!StringUtils.isEmpty(rxDeviceInfo.getManufacturer())) {
            device.setManufacturer(rxDeviceInfo.getManufacturer());
        }
        if (rxDeviceInfo.getToolId() != 0) {
            device.setToolId(rxDeviceInfo.getToolId());
        }
        if (rxDeviceInfo.getCreateTime() != 0) {
            device.setCreateTime(rxDeviceInfo.getCreateTime());
        }
        device.setUserId(userId);
        deviceService.updateDevice(device);

        ResponseData responseData = new ResponseData(ResponseCode.SUCCESS);
        responseData.addData("token", device.getToken());
        return responseData;
    }

    @PostMapping("/saveManyu2Lock")
    public ResponseData saveManyu2Lock(@RequestBody RxSaveManyu2Lock rxSaveManyu2Lock) {
        Long userId = appBean.getCurrentUserId();
        if (userId == null) {
            return ResponseData.error(ResponseCode.ERROR_ACCOUNT_NOT_LOGIN);
        }
        try {
            DeviceInfo deviceInfo = deviceService.getDeviceInfoByMac(rxSaveManyu2Lock.getMac());
            if (deviceInfo != null) {
                deviceInfo.setUuid(rxSaveManyu2Lock.getUuid());
                deviceInfo.setMacNum(new BigInteger(rxSaveManyu2Lock.getMac(), 16));
                deviceInfo.setToken(rxSaveManyu2Lock.getToken());
                deviceInfo.setModel(rxSaveManyu2Lock.getModel());
                deviceInfo.setHwVersion(rxSaveManyu2Lock.getHwVer());
                deviceInfo.setFwVersion(rxSaveManyu2Lock.getFwVer());
                deviceInfo.setCreateTime(System.currentTimeMillis() / 1000);
                deviceInfo.setState(3);
                deviceService.updateDevice(deviceInfo);
            } else {
                deviceService.saveManyuLock2(
                        rxSaveManyu2Lock.getUuid(),
                        rxSaveManyu2Lock.getMac(),
                        rxSaveManyu2Lock.getModel(),
                        rxSaveManyu2Lock.getToken(),
                        rxSaveManyu2Lock.getHwVer(),
                        rxSaveManyu2Lock.getFwVer()
                );
            }
            return new ResponseData(ResponseCode.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseData(ResponseCode.ERROR);
        }
    }

    @PostMapping("/getPrintData")
    public ResponseData getPrintData(@RequestBody RxGetPrintData rxGetPrintData) {
        String model = rxGetPrintData.getModel();
        int count = rxGetPrintData.getCount();
        String remark = deviceDescribeService.getDeviceRemarkByDescribe(model);
        if (StringUtils.isEmpty(remark)) {
            return ResponseData.error(ResponseCode.ERROR_DEVICE_MODEL_NOT_EXIST);
        }
        List<PrintDeviceBean> deviceList = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            String mac = deviceService.getNextMac();
            String token = CommUtil.getRandom8Hex();
            String sn = deviceService.getNextSn(model);
            deviceService.createDevice(sn, mac, token, model);
            PrintDeviceBean deviceBean = new PrintDeviceBean(remark, mac, sn);
            deviceList.add(deviceBean);
        }
        ResponseData responseData = new ResponseData(ResponseCode.SUCCESS);
        responseData.addData("deviceList", deviceList);
        return responseData;
    }

    @PostMapping("/cancelPrint")
    public ResponseData cancelPrint(@Valid @RequestBody RxCancelPrint rxCancelPrint) {
        List<String> macList = rxCancelPrint.getMacList();
        for (String mac:
             macList) {
            if (!StringUtils.isEmpty(mac))
                deviceService.deleteDeviceByMac(mac);
        }
        return new ResponseData(ResponseCode.SUCCESS);
    }

    @PostMapping("/print")
        public String print(String model, int count) {
        String remark = deviceDescribeService.getDeviceRemarkByDescribe(model);
        if (StringUtils.isEmpty(remark)) {
            return "设备型号不存在";
        }
        StringBuilder devices = new StringBuilder();
        for (int i = 0; i < count; i++) {
            String mac = deviceService.getNextMac();
            String token = CommUtil.getRandom8Hex();
            String sn = deviceService.getNextSn(model);
            deviceService.createDevice(sn, mac, token, model);
            String path = printService.createQrCodeByMac(remark, mac, sn);
            printService.print(path);
            devices.append("\r\n").append(remark).append("#").append(mac).append("#").append(sn);
        }
        return devices.toString();
    }

    @GetMapping("/modelGroup")
    public ResponseData modelGroup() {
        try {
            List<String> models = deviceService.getModelGroup();
            ResponseData responseData = ResponseData.success();
            responseData.addData("models", models);
            return responseData;
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseData.error();
        }
    }

    @PostMapping("/listByModel")
    public ResponseData findListByModel(@RequestBody RxDeviceListByModel rxDeviceListByModel) {
        try {
            Pageable pageable = new Pageable(rxDeviceListByModel.getPageIndex(), rxDeviceListByModel.getPageSize());
            Page<DeviceInfo> deviceList = deviceService.findListByModel(pageable, rxDeviceListByModel.getModel(), rxDeviceListByModel.getSn());
            ResponseData responseData = ResponseData.success();
            responseData.addData("deviceList", deviceList.getList());
            responseData.addData("count", deviceList.getTotal());
            return responseData;
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseData.error();
        }
    }

    @PostMapping("/deleteDevices")
    public ResponseData deleteDevices(@RequestBody RxDeleteDevices rxDeleteDevices) {

        try {
            deviceService.deleteDevices(rxDeleteDevices.getMacs());
            return ResponseData.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseData.error();
        }
    }
}
