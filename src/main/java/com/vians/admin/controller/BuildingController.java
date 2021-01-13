package com.vians.admin.controller;

import com.vians.admin.model.BuildingInfo;
import com.vians.admin.model.NatureInfo;
import com.vians.admin.request.RxBuilding;
import com.vians.admin.request.RxGetNatures;
import com.vians.admin.request.RxId;
import com.vians.admin.request.query.BuildingQuery;
import com.vians.admin.response.Page;
import com.vians.admin.response.Pageable;
import com.vians.admin.response.ResponseCode;
import com.vians.admin.response.ResponseData;
import com.vians.admin.service.BuildingService;
import com.vians.admin.service.NatureService;
import com.vians.admin.web.AppBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName BuildingController
 * @Description 楼栋对外接口
 * @Author su qinglin
 * @Date 2021/1/7 17:21
 * @Version 1.0
 **/
@CrossOrigin
@RestController
@RequestMapping("/vians/building")
public class BuildingController {

    @Autowired
    AppBean appBean;

    @Resource
    NatureService natureService;

    @Resource
    BuildingService buildingService;

    @PostMapping("/natures")
    public ResponseData getNatures(@RequestBody RxGetNatures getNatures) {

        List<NatureInfo> natureInfoList = natureService.getBuildingNatures(getNatures.getProjectId());
        ResponseData responseData = new ResponseData(ResponseCode.SUCCESS);
        responseData.addData("natures", natureInfoList);
        return responseData;
    }

    @PostMapping("/add")
    public ResponseData addBuilding(@RequestBody RxBuilding building) {
        Long userId = appBean.getCurrentUserId();
        if (userId == null) {
            return new ResponseData(ResponseCode.ERROR_UN_AUTHORIZE_LOGIN);
        }
        if (buildingService.getBuildingByNameInCommunity(
                building.getBuildingName(),
                building.getCommunityId()) != null) {
            return new ResponseData(ResponseCode.ERROR_BUILDING_NAME_EXIST);
        }
        BuildingInfo buildingInfo = new BuildingInfo();
        buildingInfo.setBuildingName(building.getBuildingName());
        buildingInfo.setCreateUserId(userId);
        buildingInfo.setNatureId(building.getNatureId());
        buildingInfo.setCommunityId(building.getCommunityId());
        buildingService.addBuilding(buildingInfo);
        return new ResponseData(ResponseCode.SUCCESS);
    }

    @PostMapping("/list")
    public ResponseData getBuildingList(@RequestBody BuildingQuery query) {
        Pageable pageable = new Pageable(query.getPageIndex(), query.getPageSize());
        Page<BuildingInfo> buildingInfoPage = buildingService.getBuildingList(query.getBuildingName(), query.getCommunityId(), pageable);
        ResponseData responseData = new ResponseData(ResponseCode.SUCCESS);
        responseData.addData("list", buildingInfoPage.getList());
        responseData.addData("total", buildingInfoPage.getTotal());
        return responseData;
    }

    @PostMapping("/all")
    public ResponseData getBuildingsByCommunityId(@RequestBody RxId rxId) {
        List<BuildingInfo> buildings = buildingService.getBuildingsByCommunityId(rxId.getId());
        ResponseData responseData = new ResponseData(ResponseCode.SUCCESS);
        responseData.addData("buildings", buildings);
        return responseData;
    }

    @PostMapping("/edit")
    public ResponseData editBuilding(@RequestBody RxBuilding building) {
        if (!StringUtils.isEmpty(building.getBuildingName())) {
            BuildingInfo bi = buildingService.getBuildingByNameInCommunity(building.getBuildingName(), building.getCommunityId());
            if (bi != null && bi.getId() != building.getId()) {
                return new ResponseData(ResponseCode.ERROR_BUILDING_NAME_EXIST);
            }
        }
        BuildingInfo buildingInfo = new BuildingInfo();
        buildingInfo.setId(building.getId());
        buildingInfo.setBuildingName(building.getBuildingName());
        buildingInfo.setNatureId(building.getNatureId());
        buildingInfo.setCommunityId(building.getCommunityId());
        buildingService.editBuilding(buildingInfo);
        return new ResponseData(ResponseCode.SUCCESS);
    }

    @PostMapping("/delete")
    public ResponseData deleteBuilding(@RequestBody RxId delete) {
        buildingService.deleteBuilding(delete.getId());
        // TODO: 还应该删除底下的楼栋单元楼层房间，以及房间下的绑定关系
        return new ResponseData(ResponseCode.SUCCESS);
    }
}
