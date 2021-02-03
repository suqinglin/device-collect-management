package com.vians.admin.controller;

import com.vians.admin.model.DataDir;
import com.vians.admin.model.NatureInfo;
import com.vians.admin.model.ProjectInfo;
import com.vians.admin.request.RxId;
import com.vians.admin.request.RxProject;
import com.vians.admin.request.RxDataDir;
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

    @PostMapping("/listByPage")
    public ResponseData getProjectListByPage(@RequestBody ProjectQuery projectQuery) {
        Pageable pageable = new Pageable(projectQuery.getPageIndex(), projectQuery.getPageSize());
        Page<ProjectInfo> projectInfoPage = projectService.getProjectListByPage(projectQuery.getProjectName(), pageable);
        ResponseData responseData = new ResponseData(ResponseCode.SUCCESS);
        responseData.addData("list", projectInfoPage.getList());
        responseData.addData("total", projectInfoPage.getTotal());
        return responseData;
    }

    @PostMapping("/list")
    public ResponseData getProjectList() {
        List<ProjectInfo> projects = projectService.getProjectList();
        ResponseData responseData = new ResponseData(ResponseCode.SUCCESS);
        responseData.addData("projects", projects);
        return responseData;
    }

    @PostMapping("/dataDir")
    public ResponseData getDataDir(@RequestBody RxDataDir rxDataDir) {
        Long projectId = appBean.getProjectId();
        if (projectId == null) {
            return ResponseData.error(ResponseCode.ERROR_USER_IS_ILLEGAL);
        }
        List<DataDir> dataDir = projectService.getDataDirs(rxDataDir.getLevel(), projectId);
        ResponseData responseData = new ResponseData(ResponseCode.SUCCESS);
        responseData.addData("dataDir", dataDir);
        return responseData;
    }

    @PostMapping("/edit")
    public ResponseData editProject(@RequestBody RxProject project) {

        if (!StringUtils.isEmpty(project.getProjectName())) {
            ProjectInfo pi = projectService.getProjectByName(project.getProjectName());
            if (pi != null && pi.getId() != project.getId()) {
                return new ResponseData(ResponseCode.ERROR_PROJECT_NAME_EXIST);
            }
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

    @PostMapping("/updateAdminProject")
    public ResponseData updateAdminProject(@RequestBody RxId rxId) {
        projectService.updateAdminProject(rxId.getId());
        return ResponseData.success();
    }
}
