package com.snake.admin.auth;

import cn.dev33.satoken.stp.StpInterface;
import cn.hutool.core.collection.CollUtil;
import com.google.common.collect.Lists;
import com.snake.admin.service.system.SysUserPermissionService;
import com.snake.admin.service.system.SysUserRoleEntityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class StpInterfaceImpl implements StpInterface {

    private final SysUserPermissionService sysUserPermissionService;

    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        return sysUserPermissionService.getPermissionList(String.valueOf(loginId));
    }

    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        String userId = String.valueOf(loginId);
        SysUserRoleEntityService sysUserRoleEntityService = sysUserPermissionService.sysUserRoleEntityService();
        Set<String> currentUserRoles = sysUserRoleEntityService.getCurrentUserRoleCodes(userId);
        List<String> roles = Lists.newArrayList();
        if(CollUtil.isNotEmpty(currentUserRoles)){
            roles.addAll(currentUserRoles);
        }
        return roles;
    }
}
