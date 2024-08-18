package com.snake.admin.service.system.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.collection.CollUtil;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.snake.admin.cache.system.*;
import com.snake.admin.common.enums.SysMenuTypeEnum;
import com.snake.admin.model.system.dto.Route;
import com.snake.admin.model.system.dto.RouteMeta;
import com.snake.admin.model.system.entity.SysMenuEntity;
import com.snake.admin.model.system.entity.SysRoleEntity;
import com.snake.admin.model.system.entity.SysRoleMenuEntity;
import com.snake.admin.service.system.SysRoleEntityService;
import com.snake.admin.service.system.SysRoleMenuEntityService;
import com.snake.admin.service.system.SysUserRoleEntityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
@Slf4j
@Component
@RequiredArgsConstructor
public class SysRouteService {


    private final SysMenuCacheService sysMenuCacheService;

    private final SysRoleCacheService sysRoleCacheService;

    private final SysRoleEntityService sysRoleEntityService;

    private final SysUserRoleEntityService sysUserRoleEntityService;

    private final SysRoleMenuEntityService sysRoleMenuEntityService;


    /**
     * 查询当前用户的菜单路由信息
     * @return
     */
    public List<Route> currentUserRoutes(){

        // 查询 当前用户的菜单ID
        String userId = (String) StpUtil.getLoginId();
        // 判断当前用户是否超级管理员
        Boolean adminRole = sysRoleEntityService.containsAdminRole(userId);
        Set<String> roleIds = null;
        Set<String> menuIds = null;
        // 查询角色菜单关联关系数据
        List<SysRoleMenuEntity> sysRoleMenuEntities = sysRoleMenuEntityService.list();
        // key 为角色ID,value 为 菜单ID集合
        Map<String, Set<String>> roleIdMappingMenuIdsMap = this.queryRoleIdMappingMenuIdsMap(sysRoleMenuEntities);
        // key 为菜单ID,value 为 角色ID集合
        Map<String, Set<String>> menuIdMappingRoleIdsMap = this.queryMenuIdMappingRoleIdsMap(sysRoleMenuEntities);
        // 根据用户查询所有菜单
        if(adminRole){
            roleIds = sysRoleCacheService.getAllRoleList().stream().map(SysRoleEntity::getId).collect(Collectors.toSet());
        }else{
            roleIds = sysUserRoleEntityService.getRoleIdsByUserId(userId);
        }
        // 根据角色查询菜单ID集合
        if(adminRole){
            menuIds = sysMenuCacheService.getAllMenuList().stream()
                    .map(SysMenuEntity::getId).collect(Collectors.toSet());
        }else{
            menuIds = Sets.newHashSet();
            if(CollUtil.isNotEmpty(roleIds)){
                for (String roleId : roleIds) {
                    Set<String> tempMenuIds = roleIdMappingMenuIdsMap.get(roleId);
                    if(CollUtil.isNotEmpty(tempMenuIds)){
                        menuIds.addAll(tempMenuIds);
                    }
                }
            }
        }
        // 获取所有菜单信息
        List<SysMenuEntity> sysMenuEntities = sysMenuCacheService.readMenuFormCache(menuIds);
        if(CollUtil.isEmpty(sysMenuEntities)){
            return null;
        }
        sysMenuEntities = sysMenuEntities.stream().filter(menu->!SysMenuTypeEnum.BUTTON.getValue().equals(menu.getMenuType())).collect(Collectors.toList());
        List<Route> list = sysMenuEntities.stream()
                .map(sysMenuEntity -> menuToRoute(sysMenuEntity, menuIdMappingRoleIdsMap,adminRole))
                .collect(Collectors.toList());
        List<Route> routeList = streamToTree(list, SysMenuEntity.ROOT_PARENT);
        return routeList;

    }


    /**
     * 列表转换为树型结构
     * @param routes
     * @param parentId
     * @return
     */
    private List<Route> streamToTree(List<Route> routes, String parentId) {
        List<Route> list = routes.stream()
                .filter(item -> parentId.equals(item.getParentId()))
                .collect(Collectors.toList());

        list = list.stream().map(item->{
            item.setChildren(streamToTree(routes,item.getId()));
            return item;
        }).collect(Collectors.toList());
        return list;
    }

    private Route menuToRoute(SysMenuEntity menuEntity,Map<String, Set<String>> menuIdMappingRoleIdsMap,Boolean adminRole){
        Integer menuType = menuEntity.getMenuType();
        SysMenuTypeEnum menuTypeEnum = SysMenuTypeEnum.getInstance(menuType);
        Route router = null;
        switch (menuTypeEnum){
            case DIR -> router = entityToDir(menuEntity,menuIdMappingRoleIdsMap,adminRole);
            case MENU -> router = entityToMenu(menuEntity,menuIdMappingRoleIdsMap,adminRole);
            case IFRAME -> router = entityToIFrame(menuEntity,menuIdMappingRoleIdsMap,adminRole);
            case LINK -> router = entityToLink(menuEntity,menuIdMappingRoleIdsMap,adminRole);
//            case BUTTON -> router = entityToButton(menuEntity,menuIdMappingRoleIdsMap,adminRole);
            default -> router = defaultConvert(menuEntity);
        }
        return router;
    }


    /**
     * 组织 目录类型数据
     * @param menuEntity
     * @return
     */
    private Route entityToDir(SysMenuEntity menuEntity,Map<String, Set<String>> menuIdMappingRoleIdsMap,Boolean adminRole) {
        String menuId = menuEntity.getId();
        Integer menuType = menuEntity.getMenuType();

        Route route = new Route();
        route.setId(menuId);
        route.setMenuType(menuType);
        route.setName(menuEntity.getName());
        route.setPath(menuEntity.getPath());
        route.setParentId(menuEntity.getParentId());

        RouteMeta meta = new RouteMeta();
        meta.setIcon(menuEntity.getIcon());
        meta.setTitle(menuEntity.getTitle());
        meta.setRank(menuEntity.getRank());
        meta.setShowLink(menuEntity.getShowLink());

        Set<String> roleCodeList = Sets.newHashSet();
        // 如果是系统管理员角色
        if(adminRole){
            roleCodeList.add(SysRoleEntity.ROLE_CODE_ADMIN);
        }else{
            Set<String> roleIds = menuIdMappingRoleIdsMap.get(menuId);
            if(CollUtil.isNotEmpty(roleIds)){
                List<SysRoleEntity> sysRoleEntities = sysRoleCacheService.readRoleFormCache(roleIds);
                roleCodeList = sysRoleEntities.stream().map(SysRoleEntity::getCode).collect(Collectors.toSet());
            }
        }
        meta.setRoles(roleCodeList);

        route.setMeta(meta);
        return route;
    }

    /**
     * 组织菜单类型数据
     * @param menuEntity
     * @param adminRole
     * @return
     */
    private Route entityToMenu(SysMenuEntity menuEntity, Map<String, Set<String>> menuIdMappingRoleIdsMap,Boolean adminRole) {
        String menuId = menuEntity.getId();
        Integer menuType = menuEntity.getMenuType();

        Route route = new Route();
        route.setId(menuId);
        route.setMenuType(menuType);
        route.setName(menuEntity.getName());
        route.setPath(menuEntity.getPath());
        route.setParentId(menuEntity.getParentId());

        RouteMeta meta = new RouteMeta();
        meta.setIcon(menuEntity.getIcon());
        meta.setTitle(menuEntity.getTitle());
        meta.setRank(menuEntity.getRank());
        meta.setShowLink(menuEntity.getShowLink());

        Set<String> roleCodeList = Sets.newHashSet();
        Set<String> authList = Sets.newHashSet(menuEntity.getAuths());
        meta.setAuths(authList);
        // 如果是系统管理员角色
        if(adminRole){
            roleCodeList.add(SysRoleEntity.ROLE_CODE_ADMIN);
        }else{
            Set<String> roleIds = menuIdMappingRoleIdsMap.get(menuId);
            if(CollUtil.isNotEmpty(roleIds)){
                List<SysRoleEntity> sysRoleEntities = sysRoleCacheService.readRoleFormCache(roleIds);
                roleCodeList = sysRoleEntities.stream().map(SysRoleEntity::getCode).collect(Collectors.toSet());
            }
        }
        meta.setRoles(roleCodeList);

        route.setMeta(meta);
        return route;
    }

    /**
     * 组织 iframe 数据
     * @param menuEntity
     * @param adminRole
     * @return
     */
    private Route entityToIFrame(SysMenuEntity menuEntity, Map<String, Set<String>> menuIdMappingRoleIdsMap,Boolean adminRole) {
        String menuId = menuEntity.getId();
        Integer menuType = menuEntity.getMenuType();
        Route route = new Route();
        route.setId(menuId);
        route.setMenuType(menuType);
        route.setName(menuEntity.getName());
        route.setPath(menuEntity.getPath());
        route.setParentId(menuEntity.getParentId());
        route.setRedirect(menuEntity.getRedirect());
        route.setComponent(Route.COMPONENT_IFRAME);

        RouteMeta meta = new RouteMeta();
        meta.setIcon(menuEntity.getIcon());
        meta.setTitle(menuEntity.getTitle());
        meta.setRank(menuEntity.getRank());
        meta.setShowLink(menuEntity.getShowLink());


        meta.setFrameSrc(menuEntity.getFrameSrc());
        meta.setKeepAlive(menuEntity.getKeepAlive());

        Set<String> roleCodeList = Sets.newHashSet();
        // 如果是系统管理员角色
        if(adminRole){
            roleCodeList.add(SysRoleEntity.ROLE_CODE_ADMIN);
        }else{
            Set<String> roleIds = menuIdMappingRoleIdsMap.get(menuId);
            if(CollUtil.isNotEmpty(roleIds)){
                List<SysRoleEntity> sysRoleEntities = sysRoleCacheService.readRoleFormCache(roleIds);
                roleCodeList = sysRoleEntities.stream().map(SysRoleEntity::getCode).collect(Collectors.toSet());
            }
        }

        meta.setRoles(roleCodeList);
        route.setMeta(meta);
        return route;
    }

    /**
     * 组织 外链 数据
     * @param menuEntity
     * @param adminRole
     * @return
     */
    private Route entityToLink(SysMenuEntity menuEntity,Map<String, Set<String>> menuIdMappingRoleIdsMap,Boolean adminRole) {
        String menuId = menuEntity.getId();
        Integer menuType = menuEntity.getMenuType();
        Route route = new Route();
        route.setId(menuId);
        route.setParentId(menuEntity.getParentId());
        route.setMenuType(menuType);
        route.setName(menuEntity.getName());
        route.setPath(menuEntity.getPath());


        RouteMeta meta = new RouteMeta();
        meta.setIcon(menuEntity.getIcon());
        meta.setTitle(menuEntity.getTitle());
        meta.setRank(menuEntity.getRank());
        meta.setShowLink(menuEntity.getShowLink());

        Set<String> roleCodeList = Sets.newHashSet();
        // 如果是系统管理员角色
        if(adminRole){
            roleCodeList.add(SysRoleEntity.ROLE_CODE_ADMIN);
        }else{
            Set<String> roleIds = menuIdMappingRoleIdsMap.get(menuId);
            if(CollUtil.isNotEmpty(roleIds)){
                List<SysRoleEntity> sysRoleEntities = sysRoleCacheService.readRoleFormCache(roleIds);
                roleCodeList = sysRoleEntities.stream().map(SysRoleEntity::getCode).collect(Collectors.toSet());
            }
        }
        meta.setRoles(roleCodeList);
        route.setMeta(meta);

        return route;
    }

    /**
     * 组织 按钮 数据
     * @param menuEntity
     * @return
     */
    private Route entityToButton(SysMenuEntity menuEntity,Map<String, Set<String>> menuIdMappingRoleIdsMap,Boolean adminRole) {
        String menuId = menuEntity.getId();
        Integer menuType = menuEntity.getMenuType();
        Route route = new Route();
        route.setId(menuId);
        route.setParentId(menuEntity.getParentId());
        route.setMenuType(menuType);
        route.setName(menuEntity.getName());
        route.setPath(menuEntity.getPath());


        RouteMeta meta = new RouteMeta();
        meta.setIcon(menuEntity.getIcon());
        meta.setTitle(menuEntity.getTitle());
        meta.setRank(menuEntity.getRank());
        meta.setShowLink(menuEntity.getShowLink());
        meta.setAuths(Sets.newHashSet(menuEntity.getAuths()));

        route.setMeta(meta);

        return route;
    }

    private Route defaultConvert(SysMenuEntity menuEntity) {
        return null;
    }





    private Map<String,Set<String>> queryMenuIdMappingRoleIdsMap(List<SysRoleMenuEntity> sysRoleMenuEntities){
        Map<String,Set<String>> menuIdMappingRoleIdsMap = Maps.newHashMap();
        if(CollUtil.isNotEmpty(sysRoleMenuEntities)){
            sysRoleMenuEntities.stream().forEach(sysRoleMenuEntity -> {
                String menuId = sysRoleMenuEntity.getMenuId();
                String roleId = sysRoleMenuEntity.getRoleId();
                Set<String> roleIds = menuIdMappingRoleIdsMap.get(menuId);
                if(Objects.isNull(roleIds)){
                    roleIds = Sets.newHashSet();
                }
                roleIds.add(roleId);
                menuIdMappingRoleIdsMap.put(menuId,roleIds);
            });
        }
        return menuIdMappingRoleIdsMap;
    }

    private Map<String,Set<String>> queryRoleIdMappingMenuIdsMap(List<SysRoleMenuEntity> sysRoleMenuEntities){
        Map<String,Set<String>> roleIdMappingMenuIdsMap = Maps.newHashMap();
        if(CollUtil.isNotEmpty(sysRoleMenuEntities)){
            sysRoleMenuEntities.stream().forEach(sysRoleMenuEntity -> {
                String roleId = sysRoleMenuEntity.getRoleId();
                String menuId = sysRoleMenuEntity.getMenuId();
                Set<String> menuIds = roleIdMappingMenuIdsMap.get(roleId);
                if(Objects.isNull(menuIds)){
                    menuIds = Sets.newHashSet();
                }
                menuIds.add(menuId);
                roleIdMappingMenuIdsMap.put(roleId,menuIds);
            });

        }
        return roleIdMappingMenuIdsMap;
    }
}
