package com.vians.admin.service;

import com.vians.admin.excel.ExcelUser;
import com.vians.admin.model.RoleInfo;
import com.vians.admin.model.UserDetailInfo;
import com.vians.admin.model.UserInfo;
import com.vians.admin.request.query.UserQuery;
import com.vians.admin.response.Page;

import java.util.List;

/**
 * @ClassName UserService
 * @Description TODO
 * @Author su qinglin
 * @Date 2021/1/12 15:23
 * @Version 1.0
 **/
public interface UserService {

    Page<UserDetailInfo> getUserList(UserQuery userQuery);

    Page<UserDetailInfo> getUnbindUserList(UserQuery userQuery);

    List<UserDetailInfo> getBindUserList(UserQuery userQuery);

    void addUser(UserDetailInfo userInfo);

    List<RoleInfo> getRoles();

    List<RoleInfo> getRolesWithPermissions();

    void editUser(UserDetailInfo userDetailInfo);

    void deleteUser(long id);

    void deleteUserByPhone(String phone);

    void bindUser(long roomId, long userId, long createUserId);

    void unbindUser(long id);

    void batchAddUsers(long projectId, List<ExcelUser> excelUsers);

    void saveRolePermissions(long roleId, int[] permissions);

    UserInfo getUserInfo(long id);

    void modifyPsw(long id, String psw);

    boolean checkOldPsw(long id, String psw);

    int getTenementCount(long projectId);

    int getVisitorCount(long projectId);

    int getServeCount(long projectId);

    int getManagerCount(long projectId);

    void deleteProjectManager(long projectId);

    void updateUsersRootId(long projectId, long userId);
}
