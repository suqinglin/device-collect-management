package com.vians.admin.mapper;

import com.vians.admin.model.LogDayCount;
import com.vians.admin.model.LogHourCount;
import com.vians.admin.model.LogMonthCount;
import com.vians.admin.model.UnlockLog;
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

    List<LogHourCount> unlockStatisticsByHour(
            @Param("projectId") long projectId,
            @Param("type") String type,
            @Param("startTime") long startTime,
            @Param("endTime") long endTime);

    List<LogDayCount> unlockStatisticsByDay(
            @Param("projectId") long projectId,
            @Param("type") String type,
            @Param("startTime") long startTime,
            @Param("endTime") long endTime);

    List<LogDayCount> unlockStatisticsByWeekDay(
            @Param("projectId") long projectId,
            @Param("type") String type,
            @Param("startTime") long startTime,
            @Param("endTime") long endTime);

    List<LogMonthCount> unlockStatisticsByMonth(
            @Param("projectId") long projectId,
            @Param("type") String type,
            @Param("startTime") long startTime,
            @Param("endTime") long endTime);

    void addAlarmLog(UnlockLog unlockLog);

    void deleteAlarmLogsByProjectId(@Param("projectId") long projectId);

    List<LogHourCount> getAlarmStatisticsByHour(
            @Param("projectId") long projectId,
            @Param("type") String type,
            @Param("startTime") long startTime,
            @Param("endTime") long endTime);

    List<LogDayCount> getAlarmStatisticsByDay(
            @Param("projectId") long projectId,
            @Param("type") String type,
            @Param("startTime") long startTime,
            @Param("endTime") long endTime);

    List<LogDayCount> getAlarmStatisticsByWeekDay(
            @Param("projectId") long projectId,
            @Param("type") String type,
            @Param("startTime") long startTime,
            @Param("endTime") long endTime);

    List<LogMonthCount> getAlarmStatisticsByMonth(
            @Param("projectId") long projectId,
            @Param("type") String type,
            @Param("startTime") long startTime,
            @Param("endTime") long endTime);

    List<UnlockLog> getRealTimeOperateStatistics(@Param("projectId") long projectId);

    List<UnlockLog> getRealTimeAlarmStatistics(@Param("projectId") Long projectId);
}
