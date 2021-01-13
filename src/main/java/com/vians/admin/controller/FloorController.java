package com.vians.admin.controller;

import com.vians.admin.model.FloorInfo;
import com.vians.admin.model.NatureInfo;
import com.vians.admin.request.RxFloor;
import com.vians.admin.request.RxGetNatures;
import com.vians.admin.request.RxId;
import com.vians.admin.request.query.FloorQuery;
import com.vians.admin.response.Page;
import com.vians.admin.response.Pageable;
import com.vians.admin.response.ResponseCode;
import com.vians.admin.response.ResponseData;
import com.vians.admin.service.FloorService;
import com.vians.admin.service.NatureService;
import com.vians.admin.web.AppBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName FloorController
 * @Description 楼层对外接口
 * @Author su qinglin
 * @Date 2021/1/7 17:21
 * @Version 1.0
 **/
@CrossOrigin
@RestController
@RequestMapping("/vians/floor")
public class FloorController {

    @Autowired
    AppBean appBean;

    @Resource
    NatureService natureService;

    @Resource
    FloorService floorService;

    @PostMapping("/natures")
    public ResponseData getNatures(@RequestBody RxGetNatures getNatures) {

        List<NatureInfo> natureInfoList = natureService.getFloorNatures(getNatures.getProjectId());
        ResponseData responseData = new ResponseData(ResponseCode.SUCCESS);
        responseData.addData("natures", natureInfoList);
        return responseData;
    }

    @PostMapping("/add")
    public ResponseData addFloor(@RequestBody RxFloor floor) {
        Long userId = appBean.getCurrentUserId();
        if (userId == null) {
            return new ResponseData(ResponseCode.ERROR_UN_AUTHORIZE_LOGIN);
        }
        if (floorService.getFloorByNameInUnit(floor.getFloorName(), floor.getUnitId()) != null) {
            return new ResponseData(ResponseCode.ERROR_FLOOR_NAME_EXIST);
        }
        FloorInfo floorInfo = new FloorInfo();
        floorInfo.setFloorName(floor.getFloorName());
        floorInfo.setCreateUserId(userId);
        floorInfo.setNatureId(floor.getNatureId());
        floorInfo.setUnitId(floor.getUnitId());
        floorService.addFloor(floorInfo);
        return new ResponseData(ResponseCode.SUCCESS);
    }

    @PostMapping("/list")
    public ResponseData getFloorList(@RequestBody FloorQuery query) {
        Pageable pageable = new Pageable(query.getPageIndex(), query.getPageSize());
        Page<FloorInfo> floorInfoPage = floorService.getFloorList(query.getFloorName(), query.getUnitId(), pageable);
        ResponseData responseData = new ResponseData(ResponseCode.SUCCESS);
        responseData.addData("list", floorInfoPage.getList());
        responseData.addData("total", floorInfoPage.getTotal());
        return responseData;
    }

    @PostMapping("/all")
    public ResponseData getFloorsByUnitId(@RequestBody RxId rxId) {
        List<FloorInfo> floors = floorService.getFloorsByUnitId(rxId.getId());
        ResponseData responseData = new ResponseData(ResponseCode.SUCCESS);
        responseData.addData("floors", floors);
        return responseData;
    }

    @PostMapping("/edit")
    public ResponseData editFloor(@RequestBody RxFloor floor) {
        if (!StringUtils.isEmpty(floor.getFloorName())) {
            FloorInfo fi = floorService.getFloorByNameInUnit(floor.getFloorName(), floor.getUnitId());
            if (fi != null && fi.getId() != floor.getId()) {
                return new ResponseData(ResponseCode.ERROR_FLOOR_NAME_EXIST);
            }
        }
        FloorInfo floorInfo = new FloorInfo();
        floorInfo.setId(floor.getId());
        floorInfo.setFloorName(floor.getFloorName());
        floorInfo.setNatureId(floor.getNatureId());
        floorInfo.setUnitId(floor.getUnitId());
        floorService.editFloor(floorInfo);
        return new ResponseData(ResponseCode.SUCCESS);
    }

    @PostMapping("/delete")
    public ResponseData deleteFloor(@RequestBody RxId delete) {
        floorService.deleteFloor(delete.getId());
        // TODO: 还应该删除底下的楼栋单元楼层房间，以及房间下的绑定关系
        return new ResponseData(ResponseCode.SUCCESS);
    }
}
