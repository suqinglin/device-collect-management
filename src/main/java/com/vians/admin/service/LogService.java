package com.vians.admin.service;

import com.vians.admin.model.UnlockLog;
import com.vians.admin.model.LogHourCount;

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

    List<LogHourCount> unlockStatistics(long projectId, String type);

    void addAlarmLog(UnlockLog unlockLog);

    void deleteAlarmLogsByProjectId(long projectId);

    List<LogHourCount> getAlarmStatistics(String type);

    List<UnlockLog> getRealTimeOperateStatistics(long projectId);
}
