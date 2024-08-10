package com.snake.admin.service.system.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.collection.CollUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.snake.admin.cache.system.*;
import com.snake.admin.common.enums.SysMenuTypeEnum;
import com.snake.admin.model.system.dto.Route;
import com.snake.admin.model.system.dto.RouteMeta;
import com.snake.admin.model.system.entity.SysMenuEntity;
import com.snake.admin.model.system.entity.SysRoleEntity;
import com.snake.admin.service.system.SysRoleEntityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SysRouteService {

    private final SysUserRoleCacheService sysUserRoleCacheService;

    private final SysRoleMenuCacheService sysRoleMenuCacheService;

    private final SysMenuCacheService sysMenuCacheService;

    private final SysRoleCacheService sysRoleCacheService;

    private final SysMenuRoleCacheService sysMenuRoleCacheService;

    private final SysRoleEntityService sysRoleEntityService;


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
        // 根据用户查询所有菜单
        if(adminRole){
            roleIds = sysRoleCacheService.getAllRoleList().stream().map(SysRoleEntity::getId).collect(Collectors.toSet());
        }else{
            roleIds = sysUserRoleCacheService.readUserRoles(userId);
        }
        // 根据角色查询菜单ID集合
        if(adminRole){
            menuIds = sysMenuCacheService.getAllMenuList().stream().map(SysMenuEntity::getId).collect(Collectors.toSet());
        }else{
            menuIds = sysRoleMenuCacheService.readRoleMenuIdsCache(roleIds);
        }
        // 获取所有菜单信息
        List<SysMenuEntity> sysMenuEntities = sysMenuCacheService.readMenuFormCache(menuIds);
        final List<Route> routes = Lists.newArrayList();
        sysMenuEntities.stream().forEach(sysMenuEntity -> {
            Route route = menuToRoute(sysMenuEntity,adminRole);
            routes.add(route);
        });
        List<Route> routeList = streamToTree(routes, SysMenuEntity.ROOT_PARENT);
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
                .filter(item -> item.getParentId().equals(parentId))
                .collect(Collectors.toList());

        list = list.stream().map(item->{
            item.setChildren(streamToTree(routes,item.getId()));
            return item;
        }).collect(Collectors.toList());
        return list;
    }

    private Route menuToRoute(SysMenuEntity menuEntity,Boolean adminRole){
        Integer menuType = menuEntity.getMenuType();
        SysMenuTypeEnum menuTypeEnum = SysMenuTypeEnum.getInstance(menuType);
        Route router = null;
        switch (menuTypeEnum){
            case DIR -> router = entityToDir(menuEntity);
            case MENU -> router = entityToMenu(menuEntity,adminRole);
            case IFRAME -> router = entityToIFrame(menuEntity,adminRole);
            case LINK -> router = entityToLink(menuEntity,adminRole);
            case BUTTON -> router = entityToButton(menuEntity,adminRole);
            default -> router = defaultConvert(menuEntity);
        }
        return router;
    }


    /**
     * 组织 目录类型数据
     * @param menuEntity
     * @return
     */
    private Route entityToDir(SysMenuEntity menuEntity) {
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

        route.setMeta(meta);
        return route;
    }

    /**
     * 组织菜单类型数据
     * @param menuEntity
     * @param adminRole
     * @return
     */
    private Route entityToMenu(SysMenuEntity menuEntity,Boolean adminRole) {
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

        Set<String> roleCodeList = Sets.newHashSet();
        // 如果是系统管理员角色
        if(adminRole){
            roleCodeList.add(SysRoleEntity.ROLE_CODE_ADMIN);
        }else{
            Set<String> roleIds = sysMenuRoleCacheService.readMenuRoleIdsCache(menuId);
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
    private Route entityToIFrame(SysMenuEntity menuEntity,Boolean adminRole) {
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


        meta.setFrameSrc(menuEntity.getFrameSrc());
        meta.setKeepAlive(menuEntity.getKeepAlive());

        Set<String> roleCodeList = Sets.newHashSet();
        // 如果是系统管理员角色
        if(adminRole){
            roleCodeList.add(SysRoleEntity.ROLE_CODE_ADMIN);
        }else{
            Set<String> roleIds = sysMenuRoleCacheService.readMenuRoleIdsCache(menuId);
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
    private Route entityToLink(SysMenuEntity menuEntity,Boolean adminRole) {
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

        route.setMeta(meta);

        return route;
    }

    /**
     * 组织 按钮 数据
     * @param menuEntity
     * @return
     */
    private Route entityToButton(SysMenuEntity menuEntity,Boolean adminRole) {
        //TODO
        return null;
    }

    private Route defaultConvert(SysMenuEntity menuEntity) {
        return null;
    }

}
