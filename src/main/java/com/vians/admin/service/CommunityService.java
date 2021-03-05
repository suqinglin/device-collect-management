package com.vians.admin.service;

import com.vians.admin.model.CommunityInfo;
import com.vians.admin.response.Page;
import com.vians.admin.response.Pageable;

import java.util.List;

/**
 * @ClassName CommunityService
 * @Description TODO
 * @Author su qinglin
 * @Date 2021/1/8 9:06
 * @Version 1.0
 **/
public interface CommunityService {

    void addCommunity(CommunityInfo communityInfo);

    Page<CommunityInfo> getCommunityList(String communityName, long projectId, Pageable pageable);

    void deleteCommunity(long id);

    void editCommunity(CommunityInfo communityInfo);

    CommunityInfo getCommunityByNameInProject(String communityName, long projectId);

    CommunityInfo getCommunityById(long id);

    List<CommunityInfo> getCommunitiesByProjectId(long id);

}
