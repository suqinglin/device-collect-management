package com.vians.admin.mapper;

import com.vians.admin.model.NatureInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @ClassName ProjectNatureMapper
 * @Description TODO
 * @Author su qinglin
 * @Date 2021/1/7 17:15
 * @Version 1.0
 **/
@Mapper
public interface NatureMapper {

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
