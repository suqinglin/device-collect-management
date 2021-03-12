package com.vians.admin.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.vians.admin.mapper.*;
import com.vians.admin.model.*;
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
    private RoomMapper roomMapper;

    @Resource
    private FloorMapper floorMapper;

    @Resource
    private UnitMapper unitMapper;

    @Resource
    private BuildingMapper buildingMapper;

    @Resource
    private CommunityMapper communityMapper;

    @Resource
    private ProjectMapper projectMapper;

    @Resource
    private DeviceMapper deviceMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addProject(ProjectInfo projectInfo) {
        projectInfo.setCreateTime(new Date());
        projectMapper.addProject(projectInfo);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Page<ProjectInfo> getProjectListByPage(String projectName, Pageable pageable) {
        PageHelper.startPage(pageable.getPageNumber(), pageable.getPageSize());
        List<ProjectInfo> projectInfoList = projectMapper.getProjectList(projectName);
        PageInfo<ProjectInfo> pageInfo = new PageInfo<>(projectInfoList);
        return new Page<>(projectInfoList, pageInfo.getTotal(), pageable);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteProject(long id) {
        List<CommunityInfo> communities = communityMapper.getCommunitiesByProjectId(id);
        communities.forEach(communityInfo -> {
        List<BuildingInfo> buildings = buildingMapper.getBuildingsByCommunityId(communityInfo.getId());
            buildings.forEach(buildingInfo -> {
                List<UnitInfo> units = unitMapper.getUnitsByBuildingId(buildingInfo.getId());
                units.forEach(unitInfo -> {
                    List<FloorInfo> floors = floorMapper.getFloorsByUnitId(unitInfo.getId());
                    floors.forEach(floorInfo -> {
                        List<RoomInfo> rooms = roomMapper.getRoomsByFloorId(floorInfo.getId());
                        rooms.forEach(roomInfo -> {
                            // 删除房间人员关联
                            roomMapper.deleteRoomUserByRoomId(roomInfo.getId());
                            // 删除房间设备关联
                            deviceMapper.unbindDevicesByRoomId(roomInfo.getId());
                        });
                        // 删除楼层下所有房间
                        roomMapper.deleteRoomsByFloorId(floorInfo.getId());
                    });
                    // 删除单元下所有楼层
                    floorMapper.deleteFloorsByUnitId(unitInfo.getId());
                });
                // 删除楼栋下所有单元
                unitMapper.deleteUnitsByBuildingId(buildingInfo.getId());
            });
            // 删除小区下所有楼栋
            buildingMapper.deleteBuildingsByCommunityId(communityInfo.getId());
        });
        // 删除项目下的所有小区
        communityMapper.deleteCommunitiesByProjectId(id);
        // TODO: 还需要删除项目下的设备、人员
        // 最后删除项目
        projectMapper.deleteProject(id);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<ProjectInfo> getProjectList() {
        return projectMapper.getAllProjects();
    }

    /**
     * 根据指定层级获取项目列表及下面的子集
     * 0：只获取项目
     * 1：获取项目->小区
     * 2：获取项目->小区->楼栋
     * 3：获取项目->小区->楼栋->单元
     * 4：获取项目->小区->楼栋->单元->楼层
     * 5：获取项目->小区->楼栋->单元->楼层->房间
     * @param level
     * @return
     */
    @Override
    public List<ProjectInfo> getProjectAll(int level) {
        List<ProjectInfo> projectList = projectMapper.getAllProjects();
        if (level >= 1) {
            projectList.forEach(project -> {
                List<CommunityInfo> communities = communityMapper.getCommunitiesByProjectId(project.getId());
                if (level >= 2) {
                    communities.forEach(community -> {
                        List<BuildingInfo> buildings = buildingMapper.getBuildingsByCommunityId(community.getId());
                        if (level >= 3) {
                            buildings.forEach(building -> {
                                List<UnitInfo> units = unitMapper.getUnitsByBuildingId(building.getId());
                                if (level >= 4) {
                                    units.forEach(unit -> {
                                        List<FloorInfo> floors = floorMapper.getFloorsByUnitId(unit.getId());
                                        if (level >= 5) {
                                            floors.forEach(floor -> {
                                                List<RoomInfo> rooms = roomMapper.getRoomsByFloorId(floor.getId());
                                                floor.setRooms(rooms);
                                            });
                                        }
                                        unit.setFloors(floors);
                                    });
                                }
                                building.setUnits(units);
                            });
                        }
                        community.setBuildings(buildings);
                    });
                }
                project.setCommunities(communities);
            });
        }
        return projectList;
    }

    /**
     * 根据指定层级获取项目列表及下面的子集
     * 0：只获取项目
     * 1：获取项目->小区
     * 2：获取项目->小区->楼栋
     * 3：获取项目->小区->楼栋->单元
     * 4：获取项目->小区->楼栋->单元->楼层
     * 5：获取项目->小区->楼栋->单元->楼层->房间
     * @param level
     * @param projectId
     * @return
     */
    @Override
    public List<DataDir> getDataDirs(int level, Long projectId) {
        List<DataDir> projects = projectMapper.getDataDir(projectId);
        if (level >= 1) {
            projects.forEach(project -> {
                List<DataDir> communities = communityMapper.getDataDir(project.getId());
                if (level >= 2) {
                    communities.forEach(community -> {
                        List<DataDir> buildings = buildingMapper.getDataDir(community.getId());
                        if (level >= 3) {
                            buildings.forEach(building -> {
                                List<DataDir> units = unitMapper.getDataDir(building.getId());
                                if (level >= 4) {
                                    units.forEach(unit -> {
                                        List<DataDir> floors = floorMapper.getDataDir(unit.getId());
                                        if (level >= 5) {
                                            floors.forEach(floor -> {
                                                List<DataDir> rooms = roomMapper.getDataDir(floor.getId());
                                                floor.setChildren(rooms);
                                            });
                                        }
                                        unit.setChildren(floors);
                                    });
                                }
                                building.setChildren(units);
                            });
                        }
                        community.setChildren(buildings);
                    });
                }
                project.setChildren(communities);
            });
        }
        return projects;
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

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateAdminProject(long projectId) {
        projectMapper.updateAdminProject(projectId);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateRootId(long projectId, long rootId) {
        projectMapper.updateRootId(projectId, rootId);
    }
}
