package com.vians.admin.mapper;

import com.vians.admin.model.BuildingInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @ClassName BuidingMapper
 * @Description TODO
 * @Author su qinglin
 * @Date 2021/1/8 9:51
 * @Version 1.0
 **/
@Mapper
public interface BuildingMapper {

    void addBuilding(BuildingInfo buildingInfo);

    List<BuildingInfo> getBuildingList(@Param("buildingName") String buildingName, @Param("communityId") long communityId);

    void deleteBuilding(long id);

    void editBuilding(BuildingInfo buildingInfo);

    BuildingInfo getBuildingById(long id);

    BuildingInfo getBuildingByNameInCommunity(
            @Param("buildingName") String buildingName,
            @Param("communityId") long communityId);

    List<BuildingInfo> getBuildingsByCommunityId(long id);
}
