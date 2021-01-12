package com.vians.admin.mapper;

import com.vians.admin.model.ProjectInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProjectMapper {

    void addProject(ProjectInfo projectInfo);

    // TODO: 还需要查询出项目下的房间数，创建人名称，所属项目性质名称
    List<ProjectInfo> getProjectList(@Param("projectName") String projectName);

    List<ProjectInfo> getAllProjects();

    ProjectInfo getProjectByName(@Param("projectName") String projectName);

    void deleteProject(long id);

    void editProject(ProjectInfo projectInfo);

    ProjectInfo getProjectById(long id);
}
