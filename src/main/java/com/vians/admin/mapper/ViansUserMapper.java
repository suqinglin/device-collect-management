package com.vians.admin.mapper;

import com.vians.admin.model.Permission;
import com.vians.admin.model.RoleInfo;
import com.vians.admin.model.UserDetailInfo;
import com.vians.admin.model.UserInfo;
import com.vians.admin.request.query.UserQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Mapper
public interface ViansUserMapper {

    UserInfo getUserByPhone(String phone);

    UserInfo getUserById(long id);

    UserInfo getUserByName(@Param("name") String name);

    List<UserDetailInfo> getUserList(UserQuery userQuery);

    void addUser(UserDetailInfo userInfo);

    /**
     * 获取不含admin和项目管理员的角色列表
     * @return
     */
    List<RoleInfo> getRoles();

    /**
     * 获取全部角色列表
     * @return
     */
    List<RoleInfo> getAllRoles();

    RoleInfo getRoleByName(@Param("roleName") String roleName);

    void editUser(UserDetailInfo userInfo);

    void deleteUser(long id);

    void deleteUserByPhone(String phone);

    void bindUser(@Param("roomId") long roomId,
                  @Param("userId") long userId,
                  @Param("createUserId") long createUserId,
                  @Param("createTime") Date createTime);

    void unbindUser(long id);

    List<UserDetailInfo> getUnbindUserList(UserQuery userQuery);

    List<UserDetailInfo> getBindUserList(UserQuery userQuery);

    ArrayList<Permission> getPermissionTree();

    void deleteRolePermissionsByRole(@Param("roleId") long roleId);

    void addRolePermissions(@Param("roleId") long roleId, @Param("permissionId") long permissionId, @Param("createTime") Date createTime);

    List<Integer> getPermissionIdsByRole(@Param("roleId") long roleId);

    List<String> getPermissionLabelsByUser(@Param("userId") long userId);

    List<Permission> getPermissionsByUser(@Param("userId") long userId);

    void modifyPsw(@Param("userId") long userId, @Param("psw") String psw);

    int getTenementCount(@Param("projectId") long projectId);

    int getVisitorCount(@Param("projectId") long projectId);

    int getServeCount(@Param("projectId") long projectId);

    int getManagerCount(@Param("projectId") long projectId);

    void deleteProjectManager(@Param("projectId") long projectId);

    void updateUsersRootId(@Param("projectId") long projectId, @Param("userId") long userId);
}
