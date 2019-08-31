package com.suql.devicecollect.controller;


import com.suql.devicecollect.model.ManufInfo;
import com.suql.devicecollect.request.*;
import com.suql.devicecollect.response.Page;
import com.suql.devicecollect.response.Pageable;
import com.suql.devicecollect.response.ResponseCode;
import com.suql.devicecollect.response.ResponseData;
import com.suql.devicecollect.service.ManufServer;
import com.suql.devicecollect.utils.Md5Util;
import com.suql.devicecollect.web.AppBean;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@CrossOrigin
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
            List<ManufInfo> manufList = manufServer.findAll();
            ResponseData responseData = new ResponseData(ResponseCode.SUCCESS);
            responseData.addData("manufList", manufList);
            return responseData;
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseData(ResponseCode.ERROR);
        }
    }

    @PostMapping("/pageList")
    public ResponseData list(@RequestBody RxManufList rxManufList) {
        Pageable pageable = new Pageable(rxManufList.getPageIndex(), rxManufList.getPageSize());
        Page<ManufInfo> manufInfoPage = manufServer.findByPage(pageable);
        ResponseData responseData = new ResponseData(ResponseCode.SUCCESS);
        responseData.addData("manufList", manufInfoPage.getList());
        responseData.addData("count", manufInfoPage.getTotal());
        return responseData;
    }

    @PostMapping("/add")
    public ResponseData add(@RequestBody RxAddManuf rxAddManuf) {

        Long userId = appBean.getCurrentUserId();
        if (userId == null) {
            return ResponseData.error(ResponseCode.ERROR_ACCOUNT_NOT_LOGIN);
        }
        manufServer.saveManuf(rxAddManuf.getName(), rxAddManuf.getAddress(), userId, rxAddManuf.getPassword());
        return new ResponseData(ResponseCode.SUCCESS);
    }

    @PostMapping("/modifyPwd")
    public ResponseData modifyPwd(@RequestBody RxModifyManufPwd rxModifyManufPwd) {

        ManufInfo manufInfo = manufServer.findById(rxModifyManufPwd.getManufId());
        String oldPwdMd5 = Md5Util.toMd5(rxModifyManufPwd.getOldPwd());
        if (manufInfo.getPassword().equals(oldPwdMd5)) {
            manufServer.modifyPwd(rxModifyManufPwd.getManufId(), rxModifyManufPwd.getNewPwd());
            return new ResponseData(ResponseCode.SUCCESS);
        } else {
            return new ResponseData(ResponseCode.ERROR_PWD_NOTAGREE);
        }
    }

    @PostMapping("/edit")
    public ResponseData edit(@RequestBody RxEditManuf rxEditManuf) {
        ManufInfo manufInfo = manufServer.findById(rxEditManuf.getId());
        if (manufInfo == null) {
            return new ResponseData(ResponseCode.ERROR_MANUF_NOT_EXIST);
        }
        try {
            manufServer.edit(rxEditManuf.getId(), rxEditManuf.getName(), rxEditManuf.getAddress());
            return new ResponseData(ResponseCode.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseData(ResponseCode.ERROR);
        }
    }

    @PostMapping("/deleteManufs")
    public ResponseData deleteManufs(@RequestBody RxDeleteManufs rxDeleteManufs) {
        try {
            manufServer.deleteAll(rxDeleteManufs.getManufIds());
            return new ResponseData(ResponseCode.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseData(ResponseCode.ERROR);
        }
    }
}
