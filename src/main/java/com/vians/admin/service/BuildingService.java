package com.vians.admin.service;

import com.vians.admin.model.BuildingInfo;
import com.vians.admin.response.Page;
import com.vians.admin.response.Pageable;

import java.util.List;

/**
 * @ClassName BuildingService
 * @Description TODO
 * @Author su qinglin
 * @Date 2021/1/8 9:49
 * @Version 1.0
 **/
public interface BuildingService {

    void addBuilding(BuildingInfo buildingInfo);

    Page<BuildingInfo> getBuildingList(String buildingName, long communityId, long projectId, Pageable pageable);

    void deleteBuilding(long id);

    void editBuilding(BuildingInfo buildingInfo);

    BuildingInfo getBuildingByNameInCommunity(String buildingName, long communityId);

    BuildingInfo getBuildingById(long id);

    List<BuildingInfo> getBuildingsByCommunityId(long id);

    int getBuildingCountByCommunityId(long id);
}
