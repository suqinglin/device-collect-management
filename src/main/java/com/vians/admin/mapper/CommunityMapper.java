package com.vians.admin.mapper;

import com.vians.admin.model.CommunityInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @ClassName CommunityMapper
 * @Description TODO
 * @Author su qinglin
 * @Date 2021/1/8 9:10
 * @Version 1.0
 **/
@Mapper
public interface CommunityMapper {

    void addCommunity(CommunityInfo communityInfo);

    List<CommunityInfo> getCommunityList(@Param("communityName") String communityName, @Param("projectId") long projectId);

    void deleteCommunity(long id);

    void editCommunity(CommunityInfo communityInfo);

    CommunityInfo getCommunityByNameInProject(@Param("communityName") String communityName, @Param("projectId") long projectId);

    CommunityInfo getCommunityById(long id);

    List<CommunityInfo> getCommunitiesByProjectId(@Param("id") long id);
}
