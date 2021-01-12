package com.vians.admin.service;

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

    Page<ProjectInfo> getProjectList(String projectName, Pageable pageable);

    List<ProjectInfo> getAllProjects();

    void deleteProject(long id);

    void editProject(ProjectInfo projectInfo);

    ProjectInfo getProjectByName(String projectName);

    ProjectInfo getProjectById(long id);
}
