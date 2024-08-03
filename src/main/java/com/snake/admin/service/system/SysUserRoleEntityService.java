package com.snake.admin.service.system;

import com.baomidou.mybatisplus.extension.service.IService;
import com.snake.admin.model.system.entity.SysUserRoleEntity;
import com.snake.admin.model.system.form.AuthorizedSysUserRoleForm;

import java.util.Set;

public interface SysUserRoleEntityService extends IService<SysUserRoleEntity> {
    /**
     * 基于角色 id 查询 当前角色是否已关联用户
     * @param id
     * @return
     */
    Boolean existRoleBindUser(String id);

    Set<String> getCurrentUserRoles(String userId);

    void authorizedRole(AuthorizedSysUserRoleForm form);

    Boolean containsAdminRole(String userId);
}
