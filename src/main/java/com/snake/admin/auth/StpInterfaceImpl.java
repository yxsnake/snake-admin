package com.snake.admin.auth;

import cn.dev33.satoken.stp.StpInterface;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import com.snake.admin.model.system.entity.SysMenuEntity;
import com.snake.admin.service.system.SysMenuEntityService;
import com.snake.admin.service.system.SysUserRoleEntityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class StpInterfaceImpl implements StpInterface {

    private final SysUserRoleEntityService sysUserRoleEntityService;

    private final SysMenuEntityService sysMenuEntityService;

    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        String userId = String.valueOf(loginId);
        Boolean containsAdminRole = sysUserRoleEntityService.containsAdminRole(userId);
        List<SysMenuEntity> currentMenuIds = sysMenuEntityService.getMenuListByUserId(userId, containsAdminRole);
        if(CollUtil.isEmpty(currentMenuIds)){
            return Lists.newArrayList();
        }
        return currentMenuIds.stream()
                .filter(sysMenuEntity -> StrUtil.isNotBlank(sysMenuEntity.getAuths()))
                .map(SysMenuEntity::getAuths).collect(Collectors.toList());
    }

    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        String userId = String.valueOf(loginId);
        Set<String> currentUserRoles = sysUserRoleEntityService.getCurrentUserRoles(userId);
        List<String> roles = Lists.newArrayList();
        if(CollUtil.isNotEmpty(currentUserRoles)){
            roles.addAll(currentUserRoles);
        }
        return roles;
    }
}
