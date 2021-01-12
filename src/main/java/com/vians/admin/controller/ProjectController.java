package com.vians.admin.controller;

import com.vians.admin.model.NatureInfo;
import com.vians.admin.model.ProjectInfo;
import com.vians.admin.request.RxId;
import com.vians.admin.request.RxProject;
import com.vians.admin.request.query.ProjectQuery;
import com.vians.admin.response.Page;
import com.vians.admin.response.Pageable;
import com.vians.admin.response.ResponseCode;
import com.vians.admin.response.ResponseData;
import com.vians.admin.service.NatureService;
import com.vians.admin.service.ProjectService;
import com.vians.admin.web.AppBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName ProjectController
 * @Description TODO
 * @Author su qinglin
 * @Date 2021/1/7 17:21
 * @Version 1.0
 **/
@CrossOrigin
@RestController
@RequestMapping("/vians/project")
public class ProjectController {

    @Autowired
    AppBean appBean;

    @Resource
    NatureService natureService;

    @Resource
    ProjectService projectService;

    @PostMapping("/natures")
    public ResponseData getNatures() {

        List<NatureInfo> natureInfoList = natureService.getProjectNatures();
        ResponseData responseData = new ResponseData(ResponseCode.SUCCESS);
        responseData.addData("natures", natureInfoList);
        return responseData;
    }

    @PostMapping("/add")
    public ResponseData addProject(@RequestBody RxProject project) {
        Long userId = appBean.getCurrentUserId();
        if (userId == null) {
            return new ResponseData(ResponseCode.ERROR_UN_AUTHORIZE_LOGIN);
        }
        if (projectService.getProjectByName(project.getProjectName()) != null) {
            return new ResponseData(ResponseCode.ERROR_PROJECT_NAME_EXIST);
        }
        ProjectInfo projectInfo = new ProjectInfo();
        projectInfo.setProjectName(project.getProjectName());
        projectInfo.setAddress(project.getAddress());
        projectInfo.setNatureId(project.getNatureId());
        projectInfo.setCreateUserId(userId);
        projectService.addProject(projectInfo);
        return new ResponseData(ResponseCode.SUCCESS);
    }

    @PostMapping("/list")
    public ResponseData getProjectList(@RequestBody ProjectQuery projectQuery) {
        Pageable pageable = new Pageable(projectQuery.getPageIndex(), projectQuery.getPageSize());
        Page<ProjectInfo> projectInfoPage = projectService.getProjectList(projectQuery.getProjectName(), pageable);
        ResponseData responseData = new ResponseData(ResponseCode.SUCCESS);
        responseData.addData("list", projectInfoPage.getList());
        responseData.addData("total", projectInfoPage.getTotal());
        return responseData;
    }

    @PostMapping("/all")
    public ResponseData getAllProjects() {
        List<ProjectInfo> projects = projectService.getAllProjects();
        ResponseData responseData = new ResponseData(ResponseCode.SUCCESS);
        responseData.addData("projects", projects);
        return responseData;
    }

    @PostMapping("/edit")
    public ResponseData editProject(@RequestBody RxProject project) {

        if (!StringUtils.isEmpty(project.getProjectName())
            && !projectService.getProjectById(project.getId()).getProjectName().equals(project.getProjectName())
            && projectService.getProjectByName(project.getProjectName()) != null) {
            return new ResponseData(ResponseCode.ERROR_PROJECT_NAME_EXIST);
        }

        ProjectInfo projectInfo = new ProjectInfo();
        projectInfo.setId(project.getId());
        projectInfo.setProjectName(project.getProjectName());
        projectInfo.setAddress(project.getAddress());
        projectInfo.setNatureId(project.getNatureId());
        projectService.editProject(projectInfo);
        return new ResponseData(ResponseCode.SUCCESS);
    }

    @PostMapping("/delete")
    public ResponseData deleteProject(@RequestBody RxId delete) {
        projectService.deleteProject(delete.getId());
        // TODO: 还应该删除底下的小区楼栋单元楼层房间，以及房间下的绑定关系
        return new ResponseData(ResponseCode.SUCCESS);
    }
}
