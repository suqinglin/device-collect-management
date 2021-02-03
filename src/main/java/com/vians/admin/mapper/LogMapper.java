package com.vians.admin.mapper;

import com.vians.admin.model.UnlockLog;
import com.vians.admin.model.LogHourCount;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @ClassName LogMapper
 * @Description TODO
 * @Author su qinglin
 * @Date 2021/1/21 11:33
 * @Version 1.0
 **/
@Mapper
public interface LogMapper {

    UnlockLog getUnlockLogByMacAndName(@Param("name") String name, @Param("mac") String mac);

    void addUnlockLog(UnlockLog unlockLog);

    void deleteUnlockLogsByProjectId(@Param("projectId") long projectId);

    List<LogHourCount> unlockStatistics(@Param("projectId") long projectId, @Param("type") String type);

    void addAlarmLog(UnlockLog unlockLog);

    void deleteAlarmLogsByProjectId(@Param("projectId") long projectId);

    List<LogHourCount> getAlarmStatistics(@Param("type") String type);

    List<UnlockLog> getRealTimeOperateStatistics(@Param("projectId") long projectId);
}
