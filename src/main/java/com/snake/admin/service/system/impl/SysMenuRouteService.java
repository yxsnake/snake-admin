package com.snake.admin.service.system.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.snake.admin.common.enums.SysMenuTypeEnum;
import com.snake.admin.mapper.system.SysMenuEntityMapper;
import com.snake.admin.mapper.system.SysRoleEntityMapper;
import com.snake.admin.model.system.dto.RouteMenuDTO;
import com.snake.admin.model.system.dto.RouteMenuMetaDTO;
import com.snake.admin.model.system.entity.SysMenuEntity;
import com.snake.admin.model.system.entity.SysRoleEntity;
import com.snake.admin.model.system.entity.SysRoleMenuEntity;
import com.snake.admin.service.system.SysRoleMenuEntityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SysMenuRouteService {

    private final SysMenuEntityMapper sysMenuEntityMapper;

    private final SysRoleEntityMapper sysRoleEntityMapper;

    private final SysRoleMenuEntityService sysRoleMenuEntityService;



    public RouteMenuDTO entityToDir(Boolean containsAdminRole,
                                    List<SysMenuEntity> currentUserMenuList,
                                    Map<String, Set<String>> menuAllowRolesMap,
                                    Map<String, Set<String>> buttonAllowAuthMap,
                                    SysMenuEntity menu) {
        String menuId = menu.getId();
        Integer menuType = menu.getMenuType();
        RouteMenuDTO routeMenuDTO = new RouteMenuDTO();
        routeMenuDTO.setId(menuId);
        routeMenuDTO.setMenuType(menuType);
        routeMenuDTO.setName(menu.getName());
        routeMenuDTO.setPath(menu.getPath());
        routeMenuDTO.setParentId(menu.getParentId());

        RouteMenuMetaDTO menuMetaDTO = new RouteMenuMetaDTO();
        menuMetaDTO.setIcon(menu.getIcon());
        menuMetaDTO.setTitle(menu.getTitle());
        menuMetaDTO.setRank(menu.getRank());
        routeMenuDTO.setMeta(menuMetaDTO);
        return routeMenuDTO;
    }

    public RouteMenuDTO entityToMenu(Boolean containsAdminRole,
                                     List<SysMenuEntity> currentUserMenuList,
                                     Map<String, Set<String>> menuAllowRolesMap,
                                     Map<String, Set<String>> buttonAllowAuthMap,
                                     SysMenuEntity menu) {
        String menuId = menu.getId();
        Integer menuType = menu.getMenuType();
        RouteMenuDTO routeMenuDTO = new RouteMenuDTO();
        routeMenuDTO.setId(menuId);
        routeMenuDTO.setMenuType(menuType);
        routeMenuDTO.setName(menu.getName());
        routeMenuDTO.setPath(menu.getPath());
        routeMenuDTO.setParentId(menu.getParentId());

        RouteMenuMetaDTO menuMetaDTO = new RouteMenuMetaDTO();
        menuMetaDTO.setIcon(menu.getIcon());
        menuMetaDTO.setTitle(menu.getTitle());
        menuMetaDTO.setRank(menu.getRank());

        Set<String> roles = menuAllowRolesMap.get(menuId);
        // 如果是系统管理员角色
        if(containsAdminRole){
            roles = Sets.newHashSet();
            roles.add(SysRoleEntity.ROLE_CODE_ADMIN);
        }
        menuMetaDTO.setRoles(roles);
        routeMenuDTO.setMeta(menuMetaDTO);
        return routeMenuDTO;
    }

    public RouteMenuDTO entityToIFrame(Boolean containsAdminRole,
                                       List<SysMenuEntity> currentUserMenuList,
                                       Map<String, Set<String>> menuAllowRolesMap,
                                       Map<String, Set<String>> buttonAllowAuthMap,
                                       SysMenuEntity menu) {
        String menuId = menu.getId();
        Integer menuType = menu.getMenuType();
        RouteMenuDTO routeMenuDTO = new RouteMenuDTO();
        routeMenuDTO.setId(menuId);
        routeMenuDTO.setMenuType(menuType);
        routeMenuDTO.setName(menu.getName());
        routeMenuDTO.setPath(menu.getPath());
        routeMenuDTO.setParentId(menu.getParentId());
        routeMenuDTO.setRedirect(menu.getRedirect());
        routeMenuDTO.setComponent("IFrame");

        RouteMenuMetaDTO menuMetaDTO = new RouteMenuMetaDTO();
        menuMetaDTO.setIcon(menu.getIcon());
        menuMetaDTO.setTitle(menu.getTitle());
        menuMetaDTO.setRank(menu.getRank());


        menuMetaDTO.setFrameSrc(menu.getFrameSrc());
        menuMetaDTO.setKeepAlive(menu.getKeepAlive());
        Set<String> roles = menuAllowRolesMap.get(menuId);
        // 如果是系统管理员角色
        if(containsAdminRole){
            roles = Sets.newHashSet();
            roles.add(SysRoleEntity.ROLE_CODE_ADMIN);
        }
        menuMetaDTO.setRoles(roles);
        routeMenuDTO.setMeta(menuMetaDTO);
        return routeMenuDTO;
    }

    public RouteMenuDTO entityToLink(Boolean containsAdminRole,
                                     List<SysMenuEntity> currentUserMenuList,
                                     Map<String, Set<String>> menuAllowRolesMap,
                                     Map<String, Set<String>> buttonAllowAuthMap,
                                     SysMenuEntity menu) {
        return null;
    }

    public RouteMenuDTO entityToButton(Boolean containsAdminRole,
                                       List<SysMenuEntity> currentUserMenuList,
                                       Map<String, Set<String>> menuAllowRolesMap,
                                       Map<String, Set<String>> buttonAllowAuthMap,
                                       SysMenuEntity menu) {
        return null;
    }

    public RouteMenuDTO defaultConvert(Boolean containsAdminRole,
                                     List<SysMenuEntity> currentUserMenuList,
                                     Map<String, Set<String>> menuAllowRolesMap,
                                     Map<String, Set<String>> buttonAllowAuthMap,
                                     SysMenuEntity menu) {
        String menuId = menu.getId();
        Integer menuType = menu.getMenuType();
        SysMenuTypeEnum menuTypeEnum = SysMenuTypeEnum.getInstance(menuType);
        RouteMenuDTO routeMenuDTO = new RouteMenuDTO();
        routeMenuDTO.setId(menuId);
        routeMenuDTO.setMenuType(menuType);
        routeMenuDTO.setName(menu.getName());
        routeMenuDTO.setPath(menu.getPath());
        routeMenuDTO.setParentId(menu.getParentId());
        routeMenuDTO.setComponent(menu.getComponent());

        RouteMenuMetaDTO menuMetaDTO = new RouteMenuMetaDTO();
        menuMetaDTO.setIcon(menu.getIcon());
        menuMetaDTO.setTitle(menu.getTitle());
        if(SysMenuTypeEnum.DIR.equals(menuTypeEnum)){
            menuMetaDTO.setRank(menu.getRank());
        }
        if(SysMenuTypeEnum.IFRAME.equals(menuTypeEnum)){
            routeMenuDTO.setRedirect(menu.getRedirect());
            routeMenuDTO.setComponent("IFrame");
            menuMetaDTO.setFrameSrc(menu.getFrameSrc());
            menuMetaDTO.setKeepAlive(menu.getKeepAlive());
        }

        SysMenuTypeEnum sysMenuTypeEnum = SysMenuTypeEnum.getInstance(menuType);
        if(SysMenuTypeEnum.BUTTON.equals(sysMenuTypeEnum)){
            Set<String> auths = buttonAllowAuthMap.get(menuId);
            // 如果是系统管理员角色
            if(containsAdminRole){
                auths = currentUserMenuList.stream()
                        .filter(sysMenuEntity -> SysMenuTypeEnum.BUTTON.equals(sysMenuEntity.getMenuType()))
                        .map(SysMenuEntity::getAuths).collect(Collectors.toSet());

            }
            menuMetaDTO.setAuths(auths);
        }else if(SysMenuTypeEnum.MENU.equals(sysMenuTypeEnum) || SysMenuTypeEnum.LINK.equals(sysMenuTypeEnum)){
            Set<String> roles = menuAllowRolesMap.get(menuId);
            // 如果是系统管理员角色
            if(containsAdminRole){
                roles = Sets.newHashSet();
                roles.add(SysRoleEntity.ROLE_CODE_ADMIN);
            }
            menuMetaDTO.setRoles(roles);

        }
        routeMenuDTO.setMeta(menuMetaDTO);
        return routeMenuDTO;
    }

    /**
     * 查询菜单允许访问的角色标识
     * key 为菜单， value 为当前菜单可访问的角色标识
     * @param menuIds
     * @return
     */
    private Map<String,Set<String>> getMenuAllowRoles(Set<String> menuIds){
        Map<String,Set<String>> menuRoleCodeListMap = Maps.newHashMap();
        List<SysRoleMenuEntity> roleMenuEntities = sysRoleMenuEntityService.lambdaQuery().in(SysRoleMenuEntity::getMenuId, menuIds).list();
        if(CollUtil.isEmpty(roleMenuEntities)){
            return menuRoleCodeListMap;
        }
        // 菜单与角色映射关系
        Map<String, List<SysRoleMenuEntity>> menuRoleMap = roleMenuEntities.stream().collect(Collectors.groupingBy(SysRoleMenuEntity::getMenuId));
        Set<String> roleIds = roleMenuEntities.stream().map(SysRoleMenuEntity::getRoleId).collect(Collectors.toSet());
        List<SysRoleEntity> sysRoleEntities = sysRoleEntityMapper.selectBatchIds(roleIds);
        if(CollUtil.isEmpty(sysRoleEntities)){
            return menuRoleCodeListMap;
        }
        // 角色 ID 与 角色标识 映射关系
        Map<String, String> roleMap = sysRoleEntities.stream().collect(Collectors.toMap(SysRoleEntity::getId, row -> row.getCode()));
        menuIds.stream().forEach(menuId->{
            List<SysRoleMenuEntity> sysRoleMenuEntities = menuRoleMap.get(menuId);
            Set<String> roles = Sets.newHashSet();
            sysRoleMenuEntities.stream().forEach(sysRoleMenuEntity -> {
                String roleId = sysRoleMenuEntity.getRoleId();
                String roleCode = roleMap.get(roleId);
                if(StrUtil.isNotBlank(roleCode)){
                    roles.add(roleCode);
                }
            });
            menuRoleCodeListMap.put(menuId,roles);
        });
        return menuRoleCodeListMap;
    }

    private Map<String,Set<String>> getButtonAllowAuth(Set<String> buttonIds){
        Map<String,Set<String>> buttonAllowAuthMap = Maps.newHashMap();
        if(CollUtil.isEmpty(buttonIds)){
            return buttonAllowAuthMap;
        }
        List<SysMenuEntity> sysMenuEntities = sysMenuEntityMapper.selectBatchIds(buttonIds);
        sysMenuEntities.stream().forEach(sysMenuEntity -> {
            String buttonId = sysMenuEntity.getId();
            Set<String> auths = buttonAllowAuthMap.get(buttonId);

            if(Objects.isNull(auths)){
                auths = Sets.newHashSet();
            }
            auths.add(sysMenuEntity.getAuths());
            buttonAllowAuthMap.put(buttonId,auths);
        });
        return buttonAllowAuthMap;
    }
}
