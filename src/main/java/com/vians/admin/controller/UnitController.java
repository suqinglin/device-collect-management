package com.vians.admin.controller;

import com.vians.admin.model.NatureInfo;
import com.vians.admin.model.UnitInfo;
import com.vians.admin.request.RxGetNatures;
import com.vians.admin.request.RxId;
import com.vians.admin.request.RxUnit;
import com.vians.admin.request.query.UnitQuery;
import com.vians.admin.response.Page;
import com.vians.admin.response.Pageable;
import com.vians.admin.response.ResponseCode;
import com.vians.admin.response.ResponseData;
import com.vians.admin.service.NatureService;
import com.vians.admin.service.UnitService;
import com.vians.admin.web.AppBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName UnitController
 * @Description 楼层对外接口
 * @Author su qinglin
 * @Date 2021/1/7 17:21
 * @Version 1.0
 **/
@CrossOrigin
@RestController
@RequestMapping("/vians/unit")
public class UnitController {

    @Autowired
    AppBean appBean;

    @Resource
    NatureService natureService;

    @Resource
    UnitService unitService;

    @PostMapping("/natures")
    public ResponseData getNatures(@RequestBody RxGetNatures getNatures) {

        List<NatureInfo> natureInfoList = natureService.getUnitNatures(getNatures.getProjectId());
        ResponseData responseData = new ResponseData(ResponseCode.SUCCESS);
        responseData.addData("natures", natureInfoList);
        return responseData;
    }

    @PostMapping("/add")
    public ResponseData addUnit(@RequestBody RxUnit unit) {
        Long userId = appBean.getCurrentUserId();
        if (userId == null) {
            return new ResponseData(ResponseCode.ERROR_UN_AUTHORIZE_LOGIN);
        }
        if (unitService.getUnitByNameInBuilding(
                unit.getUnitName(),
                unit.getBuildingId()) != null) {
            return new ResponseData(ResponseCode.ERROR_UNIT_NAME_EXIST);
        }
        UnitInfo unitInfo = new UnitInfo();
        unitInfo.setUnitName(unit.getUnitName());
        unitInfo.setCreateUserId(userId);
        unitInfo.setNatureId(unit.getNatureId());
        unitInfo.setBuildingId(unit.getBuildingId());
        unitService.addUnit(unitInfo);
        return new ResponseData(ResponseCode.SUCCESS);
    }

    @PostMapping("/list")
    public ResponseData getUnitList(@RequestBody UnitQuery query) {
        Pageable pageable = new Pageable(query.getPageIndex(), query.getPageSize());
        Page<UnitInfo> unitInfoPage = unitService.getUnitList(query.getUnitName(), query.getBuildingId(), pageable);
        ResponseData responseData = new ResponseData(ResponseCode.SUCCESS);
        responseData.addData("list", unitInfoPage.getList());
        responseData.addData("total", unitInfoPage.getTotal());
        return responseData;
    }

    @PostMapping("/all")
    public ResponseData getUnitsByBuildingId(@RequestBody RxId rxId) {
        List<UnitInfo> units = unitService.getUnitsByBuildingId(rxId.getId());
        ResponseData responseData = new ResponseData(ResponseCode.SUCCESS);
        responseData.addData("units", units);
        return responseData;
    }

    @PostMapping("/edit")
    public ResponseData editUnit(@RequestBody RxUnit unit) {
        if (!StringUtils.isEmpty(unit.getUnitName())
                && !unitService.getUnitById(unit.getId()).getUnitName().equals(unit.getUnitName())
                && unitService.getUnitByNameInBuilding(unit.getUnitName(), unit.getBuildingId()) != null) {
            return new ResponseData(ResponseCode.ERROR_UNIT_NAME_EXIST);
        }
        UnitInfo unitInfo = new UnitInfo();
        unitInfo.setId(unit.getId());
        unitInfo.setUnitName(unit.getUnitName());
        unitInfo.setNatureId(unit.getNatureId());
        unitInfo.setBuildingId(unit.getBuildingId());
        unitService.editUnit(unitInfo);
        return new ResponseData(ResponseCode.SUCCESS);
    }

    @PostMapping("/delete")
    public ResponseData deleteUnit(@RequestBody RxId delete) {
        unitService.deleteUnit(delete.getId());
        // TODO: 还应该删除底下的楼栋单元楼层房间，以及房间下的绑定关系
        return new ResponseData(ResponseCode.SUCCESS);
    }
}
