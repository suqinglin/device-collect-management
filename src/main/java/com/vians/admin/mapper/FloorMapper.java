package com.vians.admin.mapper;

import com.vians.admin.model.FloorInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @ClassName FloorMapper
 * @Description TODO
 * @Author su qinglin
 * @Date 2021/1/8 10:24
 * @Version 1.0
 **/

@Mapper
public interface FloorMapper {

    void addFloor(FloorInfo floorInfo);

    List<FloorInfo> getFloorList(@Param("floorName") String floorName, @Param("unitId") long unitId);

    void deleteFloor(long id);

    void editFloor(FloorInfo floorInfo);

    FloorInfo getFloorByNameInUnit(@Param("floorName") String floorName, @Param("unitId") long unitId);

    FloorInfo getFloorById(long id);

    List<FloorInfo> getFloorsByUnitId(@Param("id")long id);
}
