package com.vians.admin.service.impl;

import com.vians.admin.mapper.NatureMapper;
import com.vians.admin.model.NatureInfo;
import com.vians.admin.service.NatureService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName ProjectNatureServiceImpl
 * @Description TODO
 * @Author su qinglin
 * @Date 2021/1/7 17:20
 * @Version 1.0
 **/
@Service
public class NatureServiceImpl implements NatureService {

    @Resource
    private NatureMapper mapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<NatureInfo> getProjectNatures() {
        return mapper.getProjectNatures();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<NatureInfo> getCommunityNatures(long projectId) {
        return mapper.getCommunityNatures(projectId);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<NatureInfo> getBuildingNatures(long projectId) {
        return mapper.getBuildingNatures(projectId);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<NatureInfo> getUnitNatures(long projectId) {
        return mapper.getUnitNatures(projectId);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<NatureInfo> getFloorNatures(long projectId) {
        return mapper.getFloorNatures(projectId);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<NatureInfo> getRoomNatures(long projectId) {
        return mapper.getRoomNatures(projectId);
    }
}
