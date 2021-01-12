package com.vians.admin.service;

import com.vians.admin.model.NatureInfo;

import java.util.List;

/**
 * @ClassName ProjectNatureService
 * @Description TODO
 * @Author su qinglin
 * @Date 2021/1/7 17:19
 * @Version 1.0
 **/
public interface NatureService {

    /**
     * 获取项目性质
     * @return
     */
    List<NatureInfo> getProjectNatures();

    /**
     * 获取小区性质
     * @return
     */
    List<NatureInfo> getCommunityNatures(long projectId);

    /**
     * 获取楼栋性质
     * @return
     */
    List<NatureInfo> getBuildingNatures(long projectId);

    /**
     * 获取单元性质
     * @return
     */
    List<NatureInfo> getUnitNatures(long projectId);

    /**
     * 获取楼层性质
     * @return
     */
    List<NatureInfo> getFloorNatures(long projectId);

    /**
     * 获取房间性质
     * @return
     */
    List<NatureInfo> getRoomNatures(long projectId);

}
