package com.vians.admin.mapper;

import com.vians.admin.model.UnitInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @ClassName UnitMapper
 * @Description TODO
 * @Author su qinglin
 * @Date 2021/1/8 10:58
 * @Version 1.0
 **/
@Mapper
public interface UnitMapper {

    void addUnit(UnitInfo buildingInfo);

    List<UnitInfo> getUnitList(@Param("unitName") String unitName, @Param("buildingId") long buildingId);

    void deleteUnit(long id);

    void editUnit(UnitInfo unitInfo);

    UnitInfo getUnitByNameInBuilding(@Param("unitName") String unitName, @Param("buildingId") long buildingId);

    UnitInfo getUnitById(long id);

    List<UnitInfo> getUnitsByBuildingId(long id);
}
