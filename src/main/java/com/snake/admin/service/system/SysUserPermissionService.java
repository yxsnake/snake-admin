package com.snake.admin.service.system;

import java.util.List;
import java.util.Map;

public interface SysUserPermissionService {

    List<String> getPermissionList(String userId);
    SysUserRoleEntityService sysUserRoleEntityService();
}
