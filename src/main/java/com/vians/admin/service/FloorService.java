package com.vians.admin.service;

import com.vians.admin.model.FloorInfo;
import com.vians.admin.response.Page;
import com.vians.admin.response.Pageable;

import java.util.List;

/**
 * @ClassName FloorService
 * @Description TODO
 * @Author su qinglin
 * @Date 2021/1/8 10:22
 * @Version 1.0
 **/
public interface FloorService {

    void addFloor(FloorInfo floorInfo);

    Page<FloorInfo> getFloorList(String floorName, long unitId, Long projectId, Pageable pageable);

    void deleteFloor(long id);

    void editFloor(FloorInfo floorInfo);

    FloorInfo getFloorById(long id);

    FloorInfo getFloorByNameInUnit(String floorName, long unitId);

    List<FloorInfo> getFloorsByUnitId(long id);

    int getFloorCountByUnitId(long id);
}
