package com.vians.admin.service;

import com.vians.admin.model.UnitInfo;
import com.vians.admin.response.Page;
import com.vians.admin.response.Pageable;

import java.util.List;

/**
 * @ClassName UnitService
 * @Description TODO
 * @Author su qinglin
 * @Date 2021/1/8 10:54
 * @Version 1.0
 **/
public interface UnitService {

    void addUnit(UnitInfo unitInfo);

    Page<UnitInfo> getUnitList(String unitName, long buildingId, Pageable pageable);

    void deleteUnit(long id);

    void editUnit(UnitInfo unitInfo);

    UnitInfo getUnitByNameInBuilding(String unitName, long buildingId);

    UnitInfo getUnitById(long id);

    List<UnitInfo> getUnitsByBuildingId(long id);
}
