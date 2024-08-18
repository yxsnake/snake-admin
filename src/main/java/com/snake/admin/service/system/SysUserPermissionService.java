package com.snake.admin.service.system;

import java.util.List;

public interface SysUserPermissionService {

    List<String> getPermissionList(String userId);

    SysUserRoleEntityService sysUserRoleEntityService();
}
