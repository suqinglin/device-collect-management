package com.vians.admin.service.impl;

import com.vians.admin.mapper.LogMapper;
import com.vians.admin.model.UnlockLog;
import com.vians.admin.model.LogHourCount;
import com.vians.admin.service.LogService;
import org.springframework.stereotype.Service;

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

    @Override
    public UnlockLog getUnlockLogByMacAndName(String name, String mac) {
        return logMapper.getUnlockLogByMacAndName(name, mac);
    }

    @Override
    public void addUnlockLog(UnlockLog unlockLog) {
        logMapper.addUnlockLog(unlockLog);
    }

    @Override
    public void deleteUnlockLogsByProjectId(long projectId) {
        logMapper.deleteUnlockLogsByProjectId(projectId);
    }

    @Override
    public List<LogHourCount> unlockStatistics(long projectId, String type) {
        List<LogHourCount> logHourCounts = logMapper.unlockStatistics(projectId, type);
        return logHourCounts;
    }

    @Override
    public void addAlarmLog(UnlockLog unlockLog) {
        logMapper.addAlarmLog(unlockLog);
    }

    @Override
    public void deleteAlarmLogsByProjectId(long projectId) {
        logMapper.deleteAlarmLogsByProjectId(projectId);
    }

    @Override
    public List<LogHourCount> getAlarmStatistics(String type) {
        return logMapper.getAlarmStatistics(type);
    }

    @Override
    public List<UnlockLog> getRealTimeOperateStatistics(long projectId) {
        return logMapper.getRealTimeOperateStatistics(projectId);
    }
}
