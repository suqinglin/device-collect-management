package com.vians.admin.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.vians.admin.mapper.ProjectMapper;
import com.vians.admin.model.ProjectInfo;
import com.vians.admin.response.Page;
import com.vians.admin.response.Pageable;
import com.vians.admin.service.ProjectService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @ClassName ProjectServiceImpl
 * @Description TODO
 * @Author su qinglin
 * @Date 2021/1/7 19:22
 * @Version 1.0
 **/
@Service
public class ProjectServiceImpl implements ProjectService {

    @Resource
    private ProjectMapper projectMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addProject(ProjectInfo projectInfo) {
        projectInfo.setCreateTime(new Date());
        projectMapper.addProject(projectInfo);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Page<ProjectInfo> getProjectList(String projectName, Pageable pageable) {
        PageHelper.startPage(pageable.getPageNumber(), pageable.getPageSize());
        List<ProjectInfo> projectInfoList = projectMapper.getProjectList(projectName);
        PageInfo<ProjectInfo> pageInfo = new PageInfo<>(projectInfoList);
        return new Page<>(projectInfoList, pageInfo.getTotal(), pageable);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteProject(long id) {
        projectMapper.deleteProject(id);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<ProjectInfo> getAllProjects() {
        return projectMapper.getAllProjects();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void editProject(ProjectInfo projectInfo) {
        projectInfo.setUpdateTime(new Date());
        projectMapper.editProject(projectInfo);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ProjectInfo getProjectByName(String projectName) {
        return projectMapper.getProjectByName(projectName);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ProjectInfo getProjectById(long id) {
        return projectMapper.getProjectById(id);
    }
}
