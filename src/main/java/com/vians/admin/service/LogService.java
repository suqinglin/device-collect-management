package com.vians.admin.service;

import com.vians.admin.model.LogDayCount;
import com.vians.admin.model.LogHourCount;
import com.vians.admin.model.LogMonthCount;
import com.vians.admin.model.UnlockLog;

import java.util.List;

/**
 * @ClassName LogService
 * @Description TODO
 * @Author su qinglin
 * @Date 2021/1/21 11:31
 * @Version 1.0
 **/
public interface LogService {

    UnlockLog getUnlockLogByMacAndName(String name, String mac);

    void addUnlockLog(UnlockLog unlockLog);

    void deleteUnlockLogsByProjectId(long projectId);

    List<LogHourCount> unlockStatisticsByHour(long projectId, String action, long startTime, long endTime);

    List<LogDayCount> unlockStatisticsByDay(long projectId, String action, long startTime, long endTime);

    List<LogDayCount> unlockStatisticsByWeekDay(long projectId, String action, long startTime, long endTime);

    List<LogMonthCount> unlockStatisticsByMonth(long projectId, String action, long startTime, long endTime);

    void addAlarmLog(UnlockLog unlockLog);

    void deleteAlarmLogsByProjectId(long projectId);

    List<LogHourCount> getAlarmStatisticsByHour(long projectId, String type, long startTime, long endTime);

    List<LogDayCount> getAlarmStatisticsByDay(long projectId, String type, long startTime, long endTime);

    List<LogDayCount> getAlarmStatisticsByWeekDay(long projectId, String type, long startTime, long endTime);

    List<LogMonthCount> getAlarmStatisticsByMonth(long projectId, String type, long startTime, long endTime);

    List<UnlockLog> getRealTimeOperateStatistics(long projectId);

    List<UnlockLog> getRealTimeAlarmStatistics(Long projectId);
}
