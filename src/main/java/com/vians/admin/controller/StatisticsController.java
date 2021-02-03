package com.vians.admin.controller;

import com.alibaba.fastjson.JSONObject;
import com.vians.admin.common.CommConstants;
import com.vians.admin.model.RootUserInfo;
import com.vians.admin.model.LogHourCount;
import com.vians.admin.response.ResponseCode;
import com.vians.admin.response.ResponseData;
import com.vians.admin.response.bean.CheckLogResponse;
import com.vians.admin.service.*;
import com.vians.admin.utils.CommUtil;
import com.vians.admin.utils.HttpUtil;
import com.vians.admin.web.AppBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @ClassName StatisticsController
 * @Description TODO
 * @Author su qinglin
 * @Date 2021/2/1 20:49
 * @Version 1.0
 **/

@CrossOrigin
@RestController
@RequestMapping("/vians/statistics")
public class StatisticsController {

    @Autowired
    private AppBean appBean;

    @Resource
    RoomService roomService;

    @Resource
    DeviceService deviceService;

    @Resource
    UserService userService;

    @Resource
    public RootUserService rootUserService;

    @Resource
    LogService logService;

    @Resource
    ProjectService projectService;

    @PostMapping("/roomStatistics")
    public ResponseData getRoomStatistics() {
        Long projectId = appBean.getProjectId();
        if (projectId == null) {
            return ResponseData.error(ResponseCode.ERROR_USER_IS_ILLEGAL);
        }
        int unUseCount = roomService.getRoomCount(projectId, CommConstants.ROOM_SERVICE_ROOM_COUNT);
        int idleCount = roomService.getRoomCount(projectId, CommConstants.ROOM_STATE_IDLE);
        int checkInCount = roomService.getRoomCount(projectId, CommConstants.ROOM_STATE_CHECK_IN);
        int frozenInCount = roomService.getRoomCount(projectId, CommConstants.ROOM_STATE_FROZEN);
        int repairInCount = roomService.getRoomCount(projectId, CommConstants.ROOM_STATE_REPAIR);
        ResponseData responseData = new ResponseData(ResponseCode.SUCCESS);
        responseData.addData("unUseCount", unUseCount);
        responseData.addData("idleCount", idleCount);
        responseData.addData("checkInCount", checkInCount);
        responseData.addData("frozenInCount", frozenInCount);
        responseData.addData("repairInCount", repairInCount);
        return responseData;
    }

    @PostMapping("/deviceStatistics")
    public ResponseData getDeviceStatistics() {
        Long projectId = appBean.getProjectId();
        if (projectId == null) {
            return ResponseData.error(ResponseCode.ERROR_USER_IS_ILLEGAL);
        }
        Long rootId = appBean.getRootUserId();
        if (rootId == null) {
            return ResponseData.error(ResponseCode.ERROR_USER_IS_ILLEGAL);
        }
        int onlineDevCount = deviceService.getDevCount(projectId, CommConstants.DEV_STATE_ONLINE);
        int offlineDevCount = deviceService.getDevCount(projectId, CommConstants.DEV_STATE_OFFLINE);
        int onlineGwCount = deviceService.getGwCount(rootId, CommConstants.DEV_STATE_ONLINE);
        int offlineGwCount = deviceService.getGwCount(rootId, CommConstants.DEV_STATE_OFFLINE);
        ResponseData responseData = new ResponseData(ResponseCode.SUCCESS);
        responseData.addData("onlineDevCount", onlineDevCount);
        responseData.addData("offlineDevCount", offlineDevCount);
        responseData.addData("onlineGwCount", onlineGwCount);
        responseData.addData("offlineGwCount", offlineGwCount);
        return responseData;
    }

    @PostMapping("/personnelStatistics")
    public ResponseData getPersonnelStatistics() {
        Long projectId = appBean.getProjectId();
        if (projectId == null) {
            return ResponseData.error(ResponseCode.ERROR_USER_IS_ILLEGAL);
        }
        int tenementCount = userService.getTenementCount(projectId);
        int visitorCount = userService.getVisitorCount(projectId);
        int serveCount = userService.getServeCount(projectId);
        int managerCount = userService.getManagerCount(projectId);
        ResponseData responseData = new ResponseData(ResponseCode.SUCCESS);
        responseData.addData("tenementCount", tenementCount);
        responseData.addData("visitorCount", visitorCount);
        responseData.addData("serveCount", serveCount);
        responseData.addData("managerCount", managerCount);
        return responseData;
    }

    @PostMapping("/unlockStatistics")
    public ResponseData getUnlockStatistics() {
        Long rootId = appBean.getRootUserId();
        if (rootId == null) {
            return ResponseData.error(ResponseCode.ERROR_USER_IS_ILLEGAL);
        }
        Long projectId = appBean.getProjectId();
        if (projectId == null) {
            return ResponseData.error(ResponseCode.ERROR_USER_IS_ILLEGAL);
        }
        return doCheckUnlockLog(rootId, projectId, "/api/CheckLog");
    }

    @PostMapping("/realTimeOperateStatistics")
    public ResponseData getRealTimeOperateStatistics() {
        Long projectId = appBean.getProjectId();
        if (projectId == null) {
            return ResponseData.error(ResponseCode.ERROR_USER_IS_ILLEGAL);
        }
        ResponseData responseData = new ResponseData(ResponseCode.SUCCESS);
        responseData.addData("Log", logService.getRealTimeOperateStatistics(projectId));
        return responseData;
    }

    @PostMapping("/alarmStatistics")
    public ResponseData getAlarmStatistics() {
        Long rootId = appBean.getRootUserId();
        if (rootId == null) {
            return ResponseData.error(ResponseCode.ERROR_USER_IS_ILLEGAL);
        }
        Long projectId = appBean.getProjectId();
        if (projectId == null) {
            return ResponseData.error(ResponseCode.ERROR_USER_IS_ILLEGAL);
        }
        return doCheckAlarmLog(rootId, projectId, "/api/CheckAlarm");
    }

    private ResponseData doCheckUnlockLog(long rootId, long projectId, String url) {
        RootUserInfo rootUserInfo = rootUserService.getRootUserById(rootId);
        if (rootUserInfo == null) {
            return ResponseData.error(ResponseCode.ERROR_USER_IS_ILLEGAL);
        }
        Map<String, Object> requestParam = HttpUtil.getInstance().getRmtOperateCommParams(
                rootUserInfo, "0", "", "");
        if (requestParam != null) {
            long todayStartTime = CommUtil.getTodayStartTime() / 1000;
            // 2. 组装特有请求参数
            requestParam.put("BeginTime", String.valueOf(todayStartTime));
            requestParam.put("EndTime", String.valueOf(System.currentTimeMillis() / 1000));
            requestParam.put("Page", "1");
            requestParam.put("MAC", "");
            requestParam.put("PageSize", "65535");
            // 3. 发送请求
            JSONObject respObj =HttpUtil.getInstance().doRequest(requestParam, CommConstants.C_API_URL + url);
            if (respObj != null) {
                int result = (int) respObj.get("Result");
                if (result == 0) {
                    // 4. 解析出响应数据
                    CheckLogResponse checkLogResponse = respObj.toJavaObject(CheckLogResponse.class);
                    // 5. 保存cnt到DB
                    rootUserInfo.setCnt(Long.parseLong(checkLogResponse.getCNT(), 16));
                    rootUserService.updateRootUser(rootUserInfo);
                    ResponseData responseData = new ResponseData(ResponseCode.SUCCESS);
                    List<Integer> rmtUnlockCount = null;
                    List<Integer> otherUnlockCount = null;
                    List<Integer> cardUnlockCount = null;
                    List<Integer> pswUnlockCount = null;
                    List<Integer> fpUnlockCount = null;
                    List<Integer> faceUnlockCount = null;
                    List<Integer> btUnlockCount = null;
                    if (checkLogResponse.getLog() != null) {
                        // 删除
                        logService.deleteUnlockLogsByProjectId(projectId);
                        checkLogResponse.getLog().forEach(log -> {
                            log.setProjectId(projectId);
                            log.setTime(CommUtil.parseTime(Long.parseLong(log.getTime())));
                            // 添加
                            logService.addUnlockLog(log);
                        });
                        rmtUnlockCount = parseHourCount(logService.unlockStatistics(projectId, "RMT_UNLOCK"));
                        otherUnlockCount = parseHourCount(logService.unlockStatistics(projectId, "OTHER_UNLOCK"));
                        cardUnlockCount = parseHourCount(logService.unlockStatistics(projectId, "CARD_UNLOCK"));
                        pswUnlockCount = parseHourCount(logService.unlockStatistics(projectId, "PSW_UNLOCK"));
                        fpUnlockCount = parseHourCount(logService.unlockStatistics(projectId, "FP_UNLOCK"));
                        faceUnlockCount = parseHourCount(logService.unlockStatistics(projectId, "FACE_UNLOCK"));
                        btUnlockCount = parseHourCount(logService.unlockStatistics(projectId, "BT_UNLOCK"));
                    }
                    // 6. 返回数据给客户端
                    responseData.addData("rmtUnlockCount", rmtUnlockCount);
                    responseData.addData("otherUnlockCount", otherUnlockCount);
                    responseData.addData("cardUnlockCount", cardUnlockCount);
                    responseData.addData("pswUnlockCount", pswUnlockCount);
                    responseData.addData("fpUnlockCount", fpUnlockCount);
                    responseData.addData("faceUnlockCount", faceUnlockCount);
                    responseData.addData("btUnlockCount", btUnlockCount);
                    responseData.addData("title", projectService.getProjectById(projectId).getProjectName() +
                            " " + CommUtil.parseTime(new Date().getTime() / 1000, "yyyy年MM月dd日"));
                    return responseData;
                } else if (result == 1002) {
                    return HttpUtil.getInstance().reLogin(rootUserInfo.getPhone(), rootUserInfo.getPassword(), rootUserService, new HttpUtil.ReLoginCallback() {
                        @Override
                        public ResponseData success() {
                            return doCheckUnlockLog(rootId, projectId, url);
                        }

                        @Override
                        public ResponseData failure(int result) {
                            return HttpUtil.getInstance().doResult(result);
                        }
                    });
                } else {
                    return HttpUtil.getInstance().doResult(result);
                }
            } else {
                return new ResponseData(ResponseCode.ERROR);
            }
        } else {
            return new ResponseData(ResponseCode.ERROR);
        }
    }

    private ResponseData doCheckAlarmLog(long rootId, long projectId, String url) {
        RootUserInfo rootUserInfo = rootUserService.getRootUserById(rootId);
        if (rootUserInfo == null) {
            return ResponseData.error(ResponseCode.ERROR_USER_IS_ILLEGAL);
        }
        Map<String, Object> requestParam = HttpUtil.getInstance().getRmtOperateCommParams(
                rootUserInfo, "0", "", "");
        if (requestParam != null) {
            long todayStartTime = CommUtil.getTodayStartTime() / 1000;
            // 2. 组装特有请求参数
            requestParam.put("BeginTime", String.valueOf(todayStartTime));
            requestParam.put("EndTime", String.valueOf(System.currentTimeMillis() / 1000));
            requestParam.put("Page", "1");
            requestParam.put("MAC", "");
            requestParam.put("PageSize", "65535");
            // 3. 发送请求
            JSONObject respObj =HttpUtil.getInstance().doRequest(requestParam, CommConstants.C_API_URL + url);
            if (respObj != null) {
                int result = (int) respObj.get("Result");
                if (result == 0) {
                    // 4. 解析出响应数据
                    CheckLogResponse checkLogResponse = respObj.toJavaObject(CheckLogResponse.class);
                    // 5. 保存cnt到DB
                    rootUserInfo.setCnt(Long.parseLong(checkLogResponse.getCNT(), 16));
                    rootUserService.updateRootUser(rootUserInfo);
                    ResponseData responseData = new ResponseData(ResponseCode.SUCCESS);
                    List<Integer> rmtUnlockCount = null;
                    List<Integer> otherUnlockCount = null;
                    List<Integer> cardUnlockCount = null;
                    List<Integer> pswUnlockCount = null;
                    List<Integer> fpUnlockCount = null;
                    List<Integer> faceUnlockCount = null;
                    List<Integer> btUnlockCount = null;
                    if (checkLogResponse.getLog() != null) {
                        // 删除
                        logService.deleteUnlockLogsByProjectId(projectId);
                        checkLogResponse.getLog().forEach(log -> {
                            log.setProjectId(projectId);
                            log.setTime(CommUtil.parseTime(Long.parseLong(log.getTime())));
                            // 添加
                            logService.addUnlockLog(log);
                        });
                        rmtUnlockCount = parseHourCount(logService.unlockStatistics(projectId, "RMT_UNLOCK"));
                        otherUnlockCount = parseHourCount(logService.unlockStatistics(projectId, "OTHER_UNLOCK"));
                        cardUnlockCount = parseHourCount(logService.unlockStatistics(projectId, "CARD_UNLOCK"));
                        pswUnlockCount = parseHourCount(logService.unlockStatistics(projectId, "PSW_UNLOCK"));
                        fpUnlockCount = parseHourCount(logService.unlockStatistics(projectId, "FP_UNLOCK"));
                        faceUnlockCount = parseHourCount(logService.unlockStatistics(projectId, "FACE_UNLOCK"));
                        btUnlockCount = parseHourCount(logService.unlockStatistics(projectId, "BT_UNLOCK"));
                    }
                    // 6. 返回数据给客户端
                    responseData.addData("rmtUnlockCount", rmtUnlockCount);
                    responseData.addData("otherUnlockCount", otherUnlockCount);
                    responseData.addData("cardUnlockCount", cardUnlockCount);
                    responseData.addData("pswUnlockCount", pswUnlockCount);
                    responseData.addData("fpUnlockCount", fpUnlockCount);
                    responseData.addData("faceUnlockCount", faceUnlockCount);
                    responseData.addData("btUnlockCount", btUnlockCount);
                    responseData.addData("title", projectService.getProjectById(projectId).getProjectName() +
                            " " + CommUtil.parseTime(new Date().getTime() / 1000, "yyyy年MM月dd日"));
                    return responseData;
                } else if (result == 1002) {
                    return HttpUtil.getInstance().reLogin(rootUserInfo.getPhone(), rootUserInfo.getPassword(), rootUserService, new HttpUtil.ReLoginCallback() {
                        @Override
                        public ResponseData success() {
                            return doCheckUnlockLog(rootId, projectId, url);
                        }

                        @Override
                        public ResponseData failure(int result) {
                            return HttpUtil.getInstance().doResult(result);
                        }
                    });
                } else {
                    return HttpUtil.getInstance().doResult(result);
                }
            } else {
                return new ResponseData(ResponseCode.ERROR);
            }
        } else {
            return new ResponseData(ResponseCode.ERROR);
        }
    }

    private List<Integer> parseHourCount(List<LogHourCount> logHourCounts) {
        List<Integer> counts = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            for (LogHourCount logHourCount : logHourCounts) {
                if (logHourCount.getHour() == i) {
                    counts.add(logHourCount.getCount());
                    break;
                }
            }
            if (counts.size() <= i) {
                counts.add(0);
            }
        }
        return counts;
    }
}
