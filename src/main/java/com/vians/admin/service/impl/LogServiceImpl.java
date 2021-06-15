package com.vians.admin.service.impl;

import com.vians.admin.mapper.LogMapper;
import com.vians.admin.model.LogDayCount;
import com.vians.admin.model.LogHourCount;
import com.vians.admin.model.LogMonthCount;
import com.vians.admin.model.UnlockLog;
import com.vians.admin.service.LogService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName LogServiceImpl
 * @Description TODO
 * @Author su qinglin
 * @Date 2021/1/21 11:32
 * @Version 1.0
 **/
@Service
public class LogServiceImpl implements LogService {

    @Resource
    private LogMapper logMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public UnlockLog getUnlockLogByMacAndName(String name, String mac) {
        return logMapper.getUnlockLogByMacAndName(name, mac);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addUnlockLog(UnlockLog unlockLog) {
        logMapper.addUnlockLog(unlockLog);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteUnlockLogsByProjectId(long projectId) {
        logMapper.deleteUnlockLogsByProjectId(projectId);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<LogHourCount> unlockStatisticsByHour(long projectId, String action, long startTime, long endTime) {
        return logMapper.unlockStatisticsByHour(projectId, action, startTime, endTime);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<LogDayCount> unlockStatisticsByDay(long projectId, String action, long startTime, long endTime) {
        return logMapper.unlockStatisticsByDay(projectId, action, startTime, endTime);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<LogDayCount> unlockStatisticsByWeekDay(long projectId, String action, long startTime, long endTime) {
        return logMapper.unlockStatisticsByWeekDay(projectId, action, startTime, endTime);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<LogMonthCount> unlockStatisticsByMonth(long projectId, String action, long startTime, long endTime) {
        return logMapper.unlockStatisticsByMonth(projectId, action, startTime, endTime);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addAlarmLog(UnlockLog unlockLog) {
        logMapper.addAlarmLog(unlockLog);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteAlarmLogsByProjectId(long projectId) {
        logMapper.deleteAlarmLogsByProjectId(projectId);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<LogHourCount> getAlarmStatisticsByHour(long projectId, String type, long startTime, long endTime) {
        return logMapper.getAlarmStatisticsByHour(projectId, type, startTime, endTime);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<LogDayCount> getAlarmStatisticsByDay(long projectId, String type, long startTime, long endTime) {
        return logMapper.getAlarmStatisticsByDay(projectId, type, startTime, endTime);
    }

    @Override
    public List<LogDayCount> getAlarmStatisticsByWeekDay(long projectId, String type, long startTime, long endTime) {
        return logMapper.getAlarmStatisticsByWeekDay(projectId, type, startTime, endTime);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<LogMonthCount> getAlarmStatisticsByMonth(long projectId, String type, long startTime, long endTime) {
        return logMapper.getAlarmStatisticsByMonth(projectId, type, startTime, endTime);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<UnlockLog> getRealTimeOperateStatistics(long projectId) {
        return logMapper.getRealTimeOperateStatistics(projectId);
    }

    @Override
    public List<UnlockLog> getRealTimeAlarmStatistics(Long projectId) {
        return logMapper.getRealTimeAlarmStatistics(projectId);
    }
}
