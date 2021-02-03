package com.vians.admin.controller;

import com.vians.admin.model.NatureInfo;
import com.vians.admin.model.RoomInfo;
import com.vians.admin.model.RoomModelInfo;
import com.vians.admin.request.RxGetNatures;
import com.vians.admin.request.RxId;
import com.vians.admin.request.RxRoom;
import com.vians.admin.request.RxUpdateState;
import com.vians.admin.request.query.RoomQuery;
import com.vians.admin.response.Page;
import com.vians.admin.response.Pageable;
import com.vians.admin.response.ResponseCode;
import com.vians.admin.response.ResponseData;
import com.vians.admin.service.NatureService;
import com.vians.admin.service.RoomService;
import com.vians.admin.web.AppBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName RoomController
 * @Description 房间对外接口
 * @Author su qinglin
 * @Date 2021/1/7 17:21
 * @Version 1.0
 **/
@CrossOrigin
@RestController
@RequestMapping("/vians/room")
public class RoomController {

    @Autowired
    AppBean appBean;

    @Resource
    NatureService natureService;

    @Resource
    RoomService roomService;

    @PostMapping("/natures")
    public ResponseData getNatures(@RequestBody RxGetNatures getNatures) {

        List<NatureInfo> natureInfoList = natureService.getRoomNatures(getNatures.getProjectId());
        ResponseData responseData = new ResponseData(ResponseCode.SUCCESS);
        responseData.addData("natures", natureInfoList);
        return responseData;
    }

    @PostMapping("/add")
    public ResponseData addRoom(@RequestBody RxRoom room) {
        Long userId = appBean.getCurrentUserId();
        if (userId == null) {
            return new ResponseData(ResponseCode.ERROR_UN_AUTHORIZE_LOGIN);
        }
        if (roomService.getRoomByNameInFloor(room.getRoomName(), room.getFloorId()) != null) {
            return new ResponseData(ResponseCode.ERROR_ROOM_NAME_EXIST);
        }
        RoomInfo roomInfo = new RoomInfo();
        roomInfo.setRoomName(room.getRoomName());
        roomInfo.setCreateUserId(userId);
        roomInfo.setNatureId(room.getNatureId());
        roomInfo.setFloorId(room.getFloorId());
        roomInfo.setRoomModelId(room.getRoomModelId());
        roomInfo.setArea(room.getArea());
        roomInfo.setState(room.getState());
        roomService.addRoom(roomInfo);
        return new ResponseData(ResponseCode.SUCCESS);
    }

    @PostMapping("/updateState")
    public ResponseData updateState(@RequestBody RxUpdateState roomState) {
        roomService.updateRoomState(roomState.getId(), roomState.getState());
        return ResponseData.success();
    }

    @PostMapping("/list")
    public ResponseData getRoomList(@RequestBody RoomQuery query) {
        Long projectId = appBean.getProjectId();
        if (projectId == null) {
            return ResponseData.error(ResponseCode.ERROR_USER_IS_ILLEGAL);
        }
        Pageable pageable = new Pageable(query.getPageIndex(), query.getPageSize());
        Page<RoomInfo> roomInfoPage = roomService.getRoomList(query.getRoomName(), query.getFloorId(), projectId, pageable);
        ResponseData responseData = new ResponseData(ResponseCode.SUCCESS);
        responseData.addData("list", roomInfoPage.getList());
        responseData.addData("total", roomInfoPage.getTotal());
        return responseData;
    }

    @PostMapping("/edit")
    public ResponseData editRoom(@RequestBody RxRoom room) {
        if (!StringUtils.isEmpty(room.getRoomName())) {
            RoomInfo ri = roomService.getRoomByNameInFloor(room.getRoomName(), room.getFloorId());
            if (ri != null && ri.getId() != room.getId()) {
                return new ResponseData(ResponseCode.ERROR_ROOM_NAME_EXIST);
            }
        }
        RoomInfo roomInfo = new RoomInfo();
        roomInfo.setId(room.getId());
        roomInfo.setRoomName(room.getRoomName());
        roomInfo.setNatureId(room.getNatureId());
        roomInfo.setFloorId(room.getFloorId());
        roomInfo.setRoomModelId(room.getRoomModelId());
        roomInfo.setArea(room.getArea());
        roomInfo.setState(room.getState());
        roomService.editRoom(roomInfo);
        return new ResponseData(ResponseCode.SUCCESS);
    }

    @PostMapping("/userRooms")
    public ResponseData getRoomsByUserId(@RequestBody RxId rxId) {
        List<RoomInfo> rooms = roomService.getRoomListByUserId(rxId.getId());
        ResponseData responseData = new ResponseData(ResponseCode.SUCCESS);
        responseData.addData("rooms", rooms);
        return responseData;
    }

    @PostMapping("/floorRooms")
    public ResponseData getRoomsByFloorId(@RequestBody RxId rxId) {
        List<RoomInfo> rooms = roomService.getRoomsByFloorId(rxId.getId());
        ResponseData responseData = new ResponseData(ResponseCode.SUCCESS);
        responseData.addData("rooms", rooms);
        return responseData;
    }

    @PostMapping("/delete")
    public ResponseData deleteRoom(@RequestBody RxId delete) {
        roomService.deleteRoom(delete.getId());
        // TODO: 还应该删除底下的楼栋单元楼层房间，以及房间下的绑定关系
        return new ResponseData(ResponseCode.SUCCESS);
    }

    @PostMapping("/models")
    public ResponseData getRoomModels() {
        List<RoomModelInfo> roomModelList = roomService.getRoomModels();
        ResponseData responseData = new ResponseData(ResponseCode.SUCCESS);
        responseData.addData("models", roomModelList);
        return responseData;
    }
}
