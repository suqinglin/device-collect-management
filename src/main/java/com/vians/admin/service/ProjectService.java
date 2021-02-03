package com.vians.admin.service;

import com.vians.admin.model.DataDir;
import com.vians.admin.model.ProjectInfo;
import com.vians.admin.response.Page;
import com.vians.admin.response.Pageable;

import java.util.List;

/**
 * @ClassName ProjectService
 * @Description TODO
 * @Author su qinglin
 * @Date 2021/1/7 19:20
 * @Version 1.0
 **/
public interface ProjectService {

    void addProject(ProjectInfo projectInfo);

    Page<ProjectInfo> getProjectListByPage(String projectName, Pageable pageable);

    List<ProjectInfo> getProjectList();

    List<ProjectInfo> getProjectAll(int level);

    List<DataDir> getDataDirs(int level, Long projectId);

    void deleteProject(long id);

    void editProject(ProjectInfo projectInfo);

    ProjectInfo getProjectByName(String projectName);

    ProjectInfo getProjectById(long id);

    void updateAdminProject(long projectId);
}
