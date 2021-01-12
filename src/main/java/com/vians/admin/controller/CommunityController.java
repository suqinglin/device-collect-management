package com.vians.admin.controller;

import com.vians.admin.model.CommunityInfo;
import com.vians.admin.model.NatureInfo;
import com.vians.admin.request.RxCommunity;
import com.vians.admin.request.RxGetNatures;
import com.vians.admin.request.RxId;
import com.vians.admin.request.query.CommunityQuery;
import com.vians.admin.response.Page;
import com.vians.admin.response.Pageable;
import com.vians.admin.response.ResponseCode;
import com.vians.admin.response.ResponseData;
import com.vians.admin.service.CommunityService;
import com.vians.admin.service.NatureService;
import com.vians.admin.web.AppBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName CommunityController
 * @Description 小区对外接口
 * @Author su qinglin
 * @Date 2021/1/7 17:21
 * @Version 1.0
 **/
@CrossOrigin
@RestController
@RequestMapping("/vians/community")
public class CommunityController {

    @Autowired
    AppBean appBean;

    @Resource
    NatureService natureService;

    @Resource
    CommunityService communityService;

    @PostMapping("/natures")
    public ResponseData getNatures(@RequestBody RxGetNatures getNatures) {

        List<NatureInfo> natureInfoList = natureService.getCommunityNatures(getNatures.getProjectId());
        ResponseData responseData = new ResponseData(ResponseCode.SUCCESS);
        responseData.addData("natures", natureInfoList);
        return responseData;
    }

    @PostMapping("/add")
    public ResponseData addCommunity(@RequestBody RxCommunity community) {
        Long userId = appBean.getCurrentUserId();
        if (userId == null) {
            return new ResponseData(ResponseCode.ERROR_UN_AUTHORIZE_LOGIN);
        }
        if (communityService.getCommunityByNameInProject(
                community.getCommunityName(),
                community.getProjectId()) != null) {
            return new ResponseData(ResponseCode.ERROR_COMMUNITY_NAME_EXIST);
        }
        CommunityInfo communityInfo = new CommunityInfo();
        communityInfo.setCommunityName(community.getCommunityName());
        communityInfo.setCreateUserId(userId);
        communityInfo.setNatureId(community.getNatureId());
        communityInfo.setProjectId(community.getProjectId());
        communityService.addCommunity(communityInfo);
        return new ResponseData(ResponseCode.SUCCESS);
    }

    @PostMapping("/list")
    public ResponseData getCommunityList(@RequestBody CommunityQuery query) {
        Pageable pageable = new Pageable(query.getPageIndex(), query.getPageSize());
        Page<CommunityInfo> communityInfoPage = communityService.getCommunityList(query.getCommunityName(), query.getProjectId(), pageable);
        ResponseData responseData = new ResponseData(ResponseCode.SUCCESS);
        responseData.addData("list", communityInfoPage.getList());
        responseData.addData("total", communityInfoPage.getTotal());
        return responseData;
    }

    @PostMapping("/all")
    public ResponseData getCommunitiesByProjectId(@RequestBody RxId rxId) {
        List<CommunityInfo> communities = communityService.getCommunitiesByProjectId(rxId.getId());
        ResponseData responseData = new ResponseData(ResponseCode.SUCCESS);
        responseData.addData("communities", communities);
        return responseData;
    }

    @PostMapping("/edit")
    public ResponseData editCommunity(@RequestBody RxCommunity community) {
        if (!StringUtils.isEmpty(community.getCommunityName())
                && !communityService.getCommunityById(community.getId()).getCommunityName().equals(community.getCommunityName())
                && communityService.getCommunityByNameInProject(community.getCommunityName(), community.getProjectId()) != null) {
            return new ResponseData(ResponseCode.ERROR_COMMUNITY_NAME_EXIST);
        }
        CommunityInfo communityInfo = new CommunityInfo();
        communityInfo.setId(community.getId());
        communityInfo.setCommunityName(community.getCommunityName());
        communityInfo.setNatureId(community.getNatureId());
        communityInfo.setProjectId(community.getProjectId());
        communityService.editCommunity(communityInfo);
        return new ResponseData(ResponseCode.SUCCESS);
    }

    @PostMapping("/delete")
    public ResponseData deleteCommunity(@RequestBody RxId delete) {
        communityService.deleteCommunity(delete.getId());
        // TODO: 还应该删除底下的楼栋单元楼层房间，以及房间下的绑定关系
        return new ResponseData(ResponseCode.SUCCESS);
    }
}
