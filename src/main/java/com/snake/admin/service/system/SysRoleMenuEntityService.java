package com.snake.admin.service.system;

import com.baomidou.mybatisplus.extension.service.IService;
import com.snake.admin.model.system.entity.SysRoleMenuEntity;
import com.snake.admin.model.system.form.AuthorizedSysRoleMenuForm;

/**
 * @author: snake
 * @create-time: 2024-08-01
 * @description:
 * @version: 1.0
 */
public interface SysRoleMenuEntityService extends IService<SysRoleMenuEntity> {
    /**
     * 查询 当前菜单是否已关联角色
     * @param menuId 菜单ID
     * @return
     */
    Boolean existMenuBindRole(String menuId);

    void authorizedMenu(AuthorizedSysRoleMenuForm form);
}
