package com.vians.admin.controller;

import com.vians.admin.common.CommConstants;
import com.vians.admin.model.LogDayCount;
import com.vians.admin.model.LogHourCount;
import com.vians.admin.model.LogMonthCount;
import com.vians.admin.request.RxStatisticsLog;
import com.vians.admin.response.ResponseCode;
import com.vians.admin.response.ResponseData;
import com.vians.admin.service.*;
import com.vians.admin.utils.CommUtil;
import com.vians.admin.web.AppBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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
    public ResponseData getUnlockStatistics(@RequestBody RxStatisticsLog rxStatisticsLog) {
        Long rootId = appBean.getRootUserId();
        Long projectId = appBean.getProjectId();
        if (rootId == null || projectId == null) {
            return ResponseData.error(ResponseCode.ERROR_USER_IS_ILLEGAL);
        }
        ResponseData responseData = new ResponseData(ResponseCode.SUCCESS);
        List<Integer> rmtUnlockCount = null;
        List<Integer> otherUnlockCount = null;
        List<Integer> cardUnlockCount = null;
        List<Integer> pswUnlockCount = null;
        List<Integer> fpUnlockCount = null;
        List<Integer> faceUnlockCount = null;
        List<Integer> btUnlockCount = null;
        int timeType = Integer.parseInt(rxStatisticsLog.getTimeType());
        long startTime = rxStatisticsLog.getStartTime() / 1000;
        long endTime = rxStatisticsLog.getEndTime() / 1000;
        if (timeType == CommConstants.STATISTICS_TIME_TYPE_DAY) {
            rmtUnlockCount = parseHourCount(logService.unlockStatisticsByHour(projectId, "RMT_UNLOCK", startTime, endTime));
            otherUnlockCount = parseHourCount(logService.unlockStatisticsByHour(projectId, "OTHER_UNLOCK", startTime, endTime));
            cardUnlockCount = parseHourCount(logService.unlockStatisticsByHour(projectId, "CARD_UNLOCK", startTime, endTime));
            pswUnlockCount = parseHourCount(logService.unlockStatisticsByHour(projectId, "PSW_UNLOCK", startTime, endTime));
            fpUnlockCount = parseHourCount(logService.unlockStatisticsByHour(projectId, "FP_UNLOCK", startTime, endTime));
            faceUnlockCount = parseHourCount(logService.unlockStatisticsByHour(projectId, "FACE_UNLOCK", startTime, endTime));
            btUnlockCount = parseHourCount(logService.unlockStatisticsByHour(projectId, "BT_UNLOCK", startTime, endTime));
        } else if (timeType == CommConstants.STATISTICS_TIME_TYPE_MONTH) {
            int dayMax = CommUtil.getDayOfMonth();
            rmtUnlockCount = parseDayCount(logService.unlockStatisticsByDay(projectId, "RMT_UNLOCK", startTime, endTime), dayMax);
            otherUnlockCount = parseDayCount(logService.unlockStatisticsByDay(projectId, "OTHER_UNLOCK", startTime, endTime), dayMax);
            cardUnlockCount = parseDayCount(logService.unlockStatisticsByDay(projectId, "CARD_UNLOCK", startTime, endTime), dayMax);
            pswUnlockCount = parseDayCount(logService.unlockStatisticsByDay(projectId, "PSW_UNLOCK", startTime, endTime), dayMax);
            fpUnlockCount = parseDayCount(logService.unlockStatisticsByDay(projectId, "FP_UNLOCK", startTime, endTime), dayMax);
            faceUnlockCount = parseDayCount(logService.unlockStatisticsByDay(projectId, "FACE_UNLOCK", startTime, endTime), dayMax);
            btUnlockCount = parseDayCount(logService.unlockStatisticsByDay(projectId, "BT_UNLOCK", startTime, endTime), dayMax);
        } else if (timeType == CommConstants.STATISTICS_TIME_TYPE_YEAR) {
            rmtUnlockCount = parseMonthCount(logService.unlockStatisticsByMonth(projectId, "RMT_UNLOCK", startTime, endTime));
            otherUnlockCount = parseMonthCount(logService.unlockStatisticsByMonth(projectId, "OTHER_UNLOCK", startTime, endTime));
            cardUnlockCount = parseMonthCount(logService.unlockStatisticsByMonth(projectId, "CARD_UNLOCK", startTime, endTime));
            pswUnlockCount = parseMonthCount(logService.unlockStatisticsByMonth(projectId, "PSW_UNLOCK", startTime, endTime));
            fpUnlockCount = parseMonthCount(logService.unlockStatisticsByMonth(projectId, "FP_UNLOCK", startTime, endTime));
            faceUnlockCount = parseMonthCount(logService.unlockStatisticsByMonth(projectId, "FACE_UNLOCK", startTime, endTime));
            btUnlockCount = parseMonthCount(logService.unlockStatisticsByMonth(projectId, "BT_UNLOCK", startTime, endTime));
        } else if (timeType == CommConstants.STATISTICS_TIME_TYPE_WEEK) {
            rmtUnlockCount = parseWeekDayCount(logService.unlockStatisticsByWeekDay(projectId, "RMT_UNLOCK", startTime, endTime));
            otherUnlockCount = parseWeekDayCount(logService.unlockStatisticsByWeekDay(projectId, "OTHER_UNLOCK", startTime, endTime));
            cardUnlockCount = parseWeekDayCount(logService.unlockStatisticsByWeekDay(projectId, "CARD_UNLOCK", startTime, endTime));
            pswUnlockCount = parseWeekDayCount(logService.unlockStatisticsByWeekDay(projectId, "PSW_UNLOCK", startTime, endTime));
            fpUnlockCount = parseWeekDayCount(logService.unlockStatisticsByWeekDay(projectId, "FP_UNLOCK", startTime, endTime));
            faceUnlockCount = parseWeekDayCount(logService.unlockStatisticsByWeekDay(projectId, "FACE_UNLOCK", startTime, endTime));
            btUnlockCount = parseWeekDayCount(logService.unlockStatisticsByWeekDay(projectId, "BT_UNLOCK", startTime, endTime));
        }
        // 6. 返回数据给客户端
        responseData.addData("rmtUnlockCount", rmtUnlockCount);
        responseData.addData("otherUnlockCount", otherUnlockCount);
        responseData.addData("cardUnlockCount", cardUnlockCount);
        responseData.addData("pswUnlockCount", pswUnlockCount);
        responseData.addData("fpUnlockCount", fpUnlockCount);
        responseData.addData("faceUnlockCount", faceUnlockCount);
        responseData.addData("btUnlockCount", btUnlockCount);
        // 项目名称
        String projectName = projectService.getProjectById(projectId).getProjectName();
        // 时间
        String date = CommUtil.parseTime(startTime / 1000, "yyyy年MM月dd日");
        if (timeType == CommConstants.STATISTICS_TIME_TYPE_MONTH) {
            date = date.substring(0, 8);
        } else if (timeType == CommConstants.STATISTICS_TIME_TYPE_YEAR) {
            date = date.substring(0, 5);
        } else if (timeType == CommConstants.STATISTICS_TIME_TYPE_WEEK) {
            date = date.substring(0, 8) + "第" + Calendar.getInstance().get(Calendar.WEEK_OF_MONTH) + "周";
        }
        responseData.addData("title", projectName + " " + date);
        return responseData;
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

    @PostMapping("/realTimeAlarmStatistics")
    public ResponseData getRealTimeAlarmStatistics() {
        Long projectId = appBean.getProjectId();
        if (projectId == null) {
            return ResponseData.error(ResponseCode.ERROR_USER_IS_ILLEGAL);
        }
        ResponseData responseData = new ResponseData(ResponseCode.SUCCESS);
        responseData.addData("Log", logService.getRealTimeAlarmStatistics(projectId));
        return responseData;
    }

    @PostMapping("/alarmStatistics")
    public ResponseData getAlarmStatistics(@RequestBody RxStatisticsLog rxStatisticsLog) {
        Long rootId = appBean.getRootUserId();
        if (rootId == null) {
            return ResponseData.error(ResponseCode.ERROR_USER_IS_ILLEGAL);
        }
        Long projectId = appBean.getProjectId();
        if (projectId == null) {
            return ResponseData.error(ResponseCode.ERROR_USER_IS_ILLEGAL);
        }
        ResponseData responseData = new ResponseData(ResponseCode.SUCCESS);
        int timeType = Integer.parseInt(rxStatisticsLog.getTimeType());
        List<Integer> genAlarmCount = null;
        List<Integer> pirAlarmCount = null;
        List<Integer> smokeAlarmCount = null;
        List<Integer> gasAlarmCount = null;
        List<Integer> doorAlarmCount = null;
        List<Integer> lowBattAlarmCount = null;
        long startTime = rxStatisticsLog.getStartTime() / 1000;
        long endTime = rxStatisticsLog.getEndTime() / 1000;
        if (timeType == CommConstants.STATISTICS_TIME_TYPE_DAY) {
            genAlarmCount = parseHourCount(logService.getAlarmStatisticsByHour(projectId, "GEN_ALARM", startTime, endTime));
            pirAlarmCount = parseHourCount(logService.getAlarmStatisticsByHour(projectId, "PIR_ALARM", startTime, endTime));
            smokeAlarmCount = parseHourCount(logService.getAlarmStatisticsByHour(projectId, "SMOKE_ALARM", startTime, endTime));
            gasAlarmCount = parseHourCount(logService.getAlarmStatisticsByHour(projectId, "GAS_ALARM", startTime, endTime));
            doorAlarmCount = parseHourCount(logService.getAlarmStatisticsByHour(projectId, "LOCK_ALARM", startTime, endTime));
            lowBattAlarmCount = parseHourCount(logService.getAlarmStatisticsByHour(projectId, "LOW_BATTERY_ALARM", startTime, endTime));
        } else if (timeType == CommConstants.STATISTICS_TIME_TYPE_MONTH) {
            int dayMax = CommUtil.getDayOfMonth();
            genAlarmCount = parseDayCount(logService.getAlarmStatisticsByDay(projectId, "GEN_ALARM", startTime, endTime), dayMax);
            pirAlarmCount = parseDayCount(logService.getAlarmStatisticsByDay(projectId, "PIR_ALARM", startTime, endTime), dayMax);
            smokeAlarmCount = parseDayCount(logService.getAlarmStatisticsByDay(projectId, "SMOKE_ALARM", startTime, endTime), dayMax);
            gasAlarmCount = parseDayCount(logService.getAlarmStatisticsByDay(projectId, "GAS_ALARM", startTime, endTime), dayMax);
            doorAlarmCount = parseDayCount(logService.getAlarmStatisticsByDay(projectId, "LOCK_ALARM", startTime, endTime), dayMax);
            lowBattAlarmCount = parseDayCount(logService.getAlarmStatisticsByDay(projectId, "LOW_BATTERY_ALARM", startTime, endTime), dayMax);
        } else if (timeType == CommConstants.STATISTICS_TIME_TYPE_YEAR) {
            genAlarmCount = parseMonthCount(logService.getAlarmStatisticsByMonth(projectId, "GEN_ALARM", startTime, endTime));
            pirAlarmCount = parseMonthCount(logService.getAlarmStatisticsByMonth(projectId, "PIR_ALARM", startTime, endTime));
            smokeAlarmCount = parseMonthCount(logService.getAlarmStatisticsByMonth(projectId, "SMOKE_ALARM", startTime, endTime));
            gasAlarmCount = parseMonthCount(logService.getAlarmStatisticsByMonth(projectId, "GAS_ALARM", startTime, endTime));
            doorAlarmCount = parseMonthCount(logService.getAlarmStatisticsByMonth(projectId, "LOCK_ALARM", startTime, endTime));
            lowBattAlarmCount = parseMonthCount(logService.getAlarmStatisticsByMonth(projectId, "LOW_BATTERY_ALARM", startTime, endTime));
        } else if (timeType == CommConstants.STATISTICS_TIME_TYPE_WEEK) {
            genAlarmCount = parseWeekDayCount(logService.getAlarmStatisticsByWeekDay(projectId, "GEN_ALARM", startTime, endTime));
            pirAlarmCount = parseWeekDayCount(logService.getAlarmStatisticsByWeekDay(projectId, "PIR_ALARM", startTime, endTime));
            smokeAlarmCount = parseWeekDayCount(logService.getAlarmStatisticsByWeekDay(projectId, "SMOKE_ALARM", startTime, endTime));
            gasAlarmCount = parseWeekDayCount(logService.getAlarmStatisticsByWeekDay(projectId, "GAS_ALARM", startTime, endTime));
            doorAlarmCount = parseWeekDayCount(logService.getAlarmStatisticsByWeekDay(projectId, "LOCK_ALARM", startTime, endTime));
            lowBattAlarmCount = parseWeekDayCount(logService.getAlarmStatisticsByWeekDay(projectId, "LOW_BATTERY_ALARM", startTime, endTime));
        }
        // 6. 返回数据给客户端
        responseData.addData("genAlarmCount", genAlarmCount);
        responseData.addData("pirAlarmCount", pirAlarmCount);
        responseData.addData("smokeAlarmCount", smokeAlarmCount);
        responseData.addData("gasAlarmCount", gasAlarmCount);
        responseData.addData("doorAlarmCount", doorAlarmCount);
        responseData.addData("lowBattAlarmCount", lowBattAlarmCount);
        // 项目名称
        String projectName = projectService.getProjectById(projectId).getProjectName();
        // 时间
        String date = CommUtil.parseTime(startTime, "yyyy年MM月dd日");
        if (timeType == CommConstants.STATISTICS_TIME_TYPE_MONTH) {
            date = date.substring(0, 8);
        } else if (timeType == CommConstants.STATISTICS_TIME_TYPE_YEAR) {
            date = date.substring(0, 5);
        } else if (timeType == CommConstants.STATISTICS_TIME_TYPE_WEEK) {
            date = date.substring(0, 8) + "第" + Calendar.getInstance().get(Calendar.WEEK_OF_MONTH) + "周";
        }
        responseData.addData("title", projectName + " " + date);
        return responseData;
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

    private List<Integer> parseDayCount(List<LogDayCount> logDayCounts, int maxDay) {
        List<Integer> counts = new ArrayList<>();
        for (int i = 1; i <= maxDay; i++) {
            for (LogDayCount logDayCount : logDayCounts) {
                if (logDayCount.getDay() == i) {
                    counts.add(logDayCount.getCount());
                    break;
                }
            }
            if (counts.size() < i) {
                counts.add(0);
            }
        }
        return counts;
    }

    private List<Integer> parseMonthCount(List<LogMonthCount> logMonthCounts) {
        List<Integer> counts = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            for (LogMonthCount logMonthCount : logMonthCounts) {
                if (logMonthCount.getMonth() == i) {
                    counts.add(logMonthCount.getCount());
                    break;
                }
            }
            if (counts.size() < i) {
                counts.add(0);
            }
        }
        return counts;
    }

    private List<Integer> parseWeekDayCount(List<LogDayCount> logDayCounts) {
        List<Integer> counts = new ArrayList<>();
        for (int i = 1; i <= 7; i++) {
            for (LogDayCount logDayCount : logDayCounts) {
                if (logDayCount.getDay() == i) {
                    counts.add(logDayCount.getCount());
                    break;
                }
            }
            if (counts.size() < i) {
                counts.add(0);
            }
        }
        return counts;
    }
}
