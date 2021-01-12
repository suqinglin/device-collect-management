package com.vians.admin.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.vians.admin.mapper.UnitMapper;
import com.vians.admin.model.UnitInfo;
import com.vians.admin.response.Page;
import com.vians.admin.response.Pageable;
import com.vians.admin.service.UnitService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @ClassName UnitServiceImpl
 * @Description TODO
 * @Author su qinglin
 * @Date 2021/1/8 10:56
 * @Version 1.0
 **/
@Service
public class UnitServiceImpl implements UnitService {

    @Resource
    private UnitMapper unitMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addUnit(UnitInfo unitInfo) {
        unitInfo.setCreateTime(new Date());
        unitMapper.addUnit(unitInfo);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Page<UnitInfo> getUnitList(String unitName, long buildingId, Pageable pageable) {
        PageHelper.startPage(pageable.getPageNumber(), pageable.getPageSize());
        List<UnitInfo> unitInfoList = unitMapper.getUnitList(unitName, buildingId);
        PageInfo<UnitInfo> pageInfo = new PageInfo<>(unitInfoList);
        return new Page<>(unitInfoList, pageInfo.getTotal(), pageable);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteUnit(long id) {
        unitMapper.deleteUnit(id);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void editUnit(UnitInfo unitInfo) {
        unitInfo.setUpdateTime(new Date());
        unitMapper.editUnit(unitInfo);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public UnitInfo getUnitById(long id) {
        return unitMapper.getUnitById(id);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<UnitInfo> getUnitsByBuildingId(long id) {
        return unitMapper.getUnitsByBuildingId(id);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public UnitInfo getUnitByNameInBuilding(String unitName, long buildingId) {
        return unitMapper.getUnitByNameInBuilding(unitName, buildingId);
    }
}
