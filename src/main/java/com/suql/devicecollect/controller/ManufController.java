package com.suql.devicecollect.controller;


import com.suql.devicecollect.model.ManufInfo;
import com.suql.devicecollect.response.ResponseCode;
import com.suql.devicecollect.response.ResponseData;
import com.suql.devicecollect.service.ManufServer;
import com.suql.devicecollect.web.AppBean;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/manuf")
public class ManufController {

    @Resource
    private AppBean appBean;

    @Resource
    private ManufServer manufServer;

    @PostMapping("/list")
    public ResponseData getManfList() {

        Long userId = appBean.getCurrentUserId();
        if (userId == null) {
            return ResponseData.error(ResponseCode.ERROR_ACCOUNT_NOT_LOGIN);
        }

        try {
            List<ManufInfo> manufList = manufServer.getManufList();
            ResponseData responseData = new ResponseData(ResponseCode.SUCCESS);
            responseData.addData("manufList", manufList);
            return responseData;
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseData(ResponseCode.ERROR);
        }
    }
}
