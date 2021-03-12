package com.vians.admin.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.vians.admin.excel.ExcelUser;
import com.vians.admin.mapper.ViansUserMapper;
import com.vians.admin.model.Permission;
import com.vians.admin.model.RoleInfo;
import com.vians.admin.model.UserDetailInfo;
import com.vians.admin.model.UserInfo;
import com.vians.admin.request.query.UserQuery;
import com.vians.admin.response.Page;
import com.vians.admin.response.Pageable;
import com.vians.admin.service.UserService;
import com.vians.admin.utils.Md5Util;
import com.vians.admin.utils.PermissionHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @ClassName UserDetailServiceImpl
 * @Description TODO
 * @Author su qinglin
 * @Date 2021/1/12 15:27
 * @Version 1.0
 **/
@Service
public class UserServiceImpl implements UserService {

    final Logger logger = LoggerFactory.getLogger(getClass());
//    private List<Permission> permissionList;

    @Resource
    ViansUserMapper userMapper;

//    @PostConstruct
//    public void init() {
//        permissionList = userMapper.getPermissionTree();
//    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Page<UserDetailInfo> getUserList(UserQuery userQuery) {
        Pageable pageable = new Pageable(userQuery.getPageIndex(), userQuery.getPageSize());
        PageHelper.startPage(pageable.getPageNumber(), pageable.getPageSize());
        List<UserDetailInfo> userList = userMapper.getUserList(userQuery);
        PageInfo<UserDetailInfo> pageInfo = new PageInfo<>(userList);
        return new Page<>(userList, pageInfo.getTotal(), pageable);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addUser(UserDetailInfo userInfo) {
        userInfo.setCreateTime(new Date());
        userInfo.setPassword(Md5Util.toMd5(userInfo.getPassword()));
        userMapper.addUser(userInfo);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<RoleInfo> getRoles() {
        return userMapper.getRoles();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<RoleInfo> getRolesWithPermissions() {
        List<RoleInfo> roles = userMapper.getAllRoles();
        roles.forEach(role -> {
            role.setPermissionList(PermissionHelper.getInstance(userMapper).getPermissionList());
            List<Integer> checkedPermissions = userMapper.getPermissionIdsByRole(role.getId());
            role.setCheckedPermissionIds(checkedPermissions.stream().mapToInt(Integer::valueOf).toArray());
//            role.setPermissionList(userMapper.getPermissionTree());
        });
        return roles;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void editUser(UserDetailInfo userDetailInfo) {
        userDetailInfo.setUpdateTime(new Date());
        userMapper.editUser(userDetailInfo);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteUser(long id) {
        userMapper.deleteUser(id);
    }

    @Override
    public void deleteUserByPhone(String phone) {
        userMapper.deleteUserByPhone(phone);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void bindUser(long roomId, long userId, long createUserId) {
        userMapper.bindUser(roomId, userId, createUserId, new Date());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void unbindUser(long id) {
        userMapper.unbindUser(id);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Page<UserDetailInfo> getUnbindUserList(UserQuery userQuery) {
        Pageable pageable = new Pageable(userQuery.getPageIndex(), userQuery.getPageSize());
        PageHelper.startPage(pageable.getPageNumber(), pageable.getPageSize());
        List<UserDetailInfo> userList = userMapper.getUnbindUserList(userQuery);
        PageInfo<UserDetailInfo> pageInfo = new PageInfo<>(userList);
        return new Page<>(userList, pageInfo.getTotal(), pageable);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<UserDetailInfo> getBindUserList(UserQuery userQuery) {
        return userMapper.getBindUserList(userQuery);
    }

    /**
     * 批量添加excel导入的用户
     * @param projectId
     * @param excelUsers
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void batchAddUsers(long projectId, List<ExcelUser> excelUsers) {
        excelUsers.forEach(excelUser -> {
            if (StringUtils.isEmpty(excelUser.getPhone())) {
                logger.info("userService phone is null, {}", excelUser.toString());
                return;
            }
            if (userMapper.getUserByPhone(excelUser.getPhone()) != null) {
                logger.info("userService phone is exist, {}", excelUser.toString());
                return;
            }
            if (StringUtils.isEmpty(excelUser.getRole())) {
                logger.info("userService role is null, {}", excelUser.toString());
                return;
            }
            RoleInfo roleInfo = userMapper.getRoleByName(excelUser.getRole());
            if (roleInfo == null) {
                logger.info("userService role is invalid, {}", excelUser.toString());
                return;
            }
            if (StringUtils.isEmpty(excelUser.getGender())
                    || (excelUser.getGender().equals("男") && excelUser.getGender().equals("女"))) {
                logger.info("userService gender is invalid, {}", excelUser.toString());
                return;
            }
            UserDetailInfo userDetailInfo = new UserDetailInfo();
            userDetailInfo.setProjectId(projectId);
            userDetailInfo.setPhone(excelUser.getPhone());
            userDetailInfo.setDuty(excelUser.getDuty());
            userDetailInfo.setWorkNumber(excelUser.getWorkNum());
            userDetailInfo.setDepartment(excelUser.getDepartment());
            userDetailInfo.setOrganization(excelUser.getOrganization());
            userDetailInfo.setRoleId(roleInfo.getId());
            userDetailInfo.setCardId(excelUser.getCardId());
            userDetailInfo.setUserName(excelUser.getUserName());
            userDetailInfo.setGender("男".equals(excelUser.getGender()) ? 1 : 2);
            userDetailInfo.setPassword(Md5Util.toMd5(excelUser.getPassword()));
            userDetailInfo.setCreateTime(new Date());
            userMapper.addUser(userDetailInfo);
        });
    }

    @Override
    public void saveRolePermissions(long roleId, int[] permissions) {
        userMapper.deleteRolePermissionsByRole(roleId);
        for (int permission : permissions) {
            userMapper.addRolePermissions(roleId, permission, new Date());
        }
    }

    @Override
    public UserInfo getUserInfo(long id) {
        UserInfo userInfo = userMapper.getUserById(id);
        List<Permission> permissions = userMapper.getPermissionsByUser(id);
        List<String> pLabels = new ArrayList<>();
        permissions.forEach(permission -> {
            pLabels.add(permission.getLabel());
            if (permission.getParentId() != 0 && !pLabels.contains(permission.getPLabel())) {
                pLabels.add(permission.getPLabel());
            }
        });
        userInfo.setPermissions(pLabels);
        return userInfo;
    }

    @Override
    public void modifyPsw(long id, String psw) {
        String pswMd5 = Md5Util.toMd5(psw);
        userMapper.modifyPsw(id, pswMd5);
    }

    @Override
    public boolean checkOldPsw(long id, String psw) {
        UserInfo userInfo = userMapper.getUserById(id);
        return Md5Util.toMd5(psw).equals(userInfo.getPassword());
    }

    @Override
    public int getTenementCount(long projectId) {
        return userMapper.getTenementCount(projectId);
    }

    @Override
    public int getVisitorCount(long projectId) {
        return userMapper.getVisitorCount(projectId);
    }

    @Override
    public int getServeCount(long projectId) {
        return userMapper.getServeCount(projectId);
    }

    @Override
    public int getManagerCount(long projectId) {
        return userMapper.getManagerCount(projectId);
    }

    /**
     * 删除项目中的项目管理员账号，确保每个项目中管理员唯一
     * @param projectId
     */
    @Override
    public void deleteProjectManager(long projectId) {
        userMapper.deleteProjectManager(projectId);
    }

    @Override
    public void updateUsersRootId(long projectId, long userId) {
        userMapper.updateUsersRootId(projectId, userId);
    }

}
