package com.snake.admin.service.system.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.snake.admin.cache.system.SysMenuCacheService;
import com.snake.admin.mapper.system.SysMenuEntityMapper;
import com.snake.admin.mapper.system.SysRoleMenuEntityMapper;
import com.snake.admin.model.system.entity.SysMenuEntity;
import com.snake.admin.model.system.entity.SysRoleMenuEntity;
import com.snake.admin.model.system.form.AuthorizedSysRoleMenuForm;
import com.snake.admin.service.system.SysRoleMenuEntityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author: snake
 * @create-time: 2024-08-01
 * @description:
 * @version: 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SysRoleMenuEntityServiceImpl extends ServiceImpl<SysRoleMenuEntityMapper, SysRoleMenuEntity> implements SysRoleMenuEntityService {

    private final SysMenuEntityMapper sysMenuEntityMapper;

    private final SysMenuCacheService sysMenuCacheService;

    @Override
    public Boolean existMenuBindRole(String id) {
        return this.lambdaQuery().eq(SysRoleMenuEntity::getMenuId,id).list().stream().count()>0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void authorizedMenu(AuthorizedSysRoleMenuForm form) {
        List<SysRoleMenuEntity> list = this.lambdaQuery().eq(SysRoleMenuEntity::getRoleId, form.getRoleId()).list();
        Set<String> oldMenuIds = null;
        if(CollUtil.isNotEmpty(list)){
            oldMenuIds = list.stream().map(SysRoleMenuEntity::getId).collect(Collectors.toSet());
            this.getBaseMapper().deleteBatchIds(oldMenuIds);
        }
        // 声明一个集合 存储 前端传递的所有菜单的 上级所有菜单ID
        Set<String> upperIds = Sets.newHashSet();
        // 查询所有菜单
        Map<String, String> menuMappingParentMap = menuMappingParentMap();
        for (String menuId : form.getMenuIds()) {
            // 通过递归找到每一个菜单的所有上级一直到顶级菜单的ID集合
            getCurrentMenuUpperIds(upperIds,menuMappingParentMap,menuId);
        }
        // 把传递过的菜单也加入
        upperIds.addAll(form.getMenuIds());
        // 反向查询角色信息，防止 不存在的角色
        List<SysMenuEntity> sysMenuEntities = sysMenuEntityMapper.selectBatchIds(upperIds);
        // 组装 用户角色关联关系对象
        List<SysRoleMenuEntity> roleMenuEntities = Lists.newArrayList();
        sysMenuEntities.stream().forEach(sysMenuEntity -> {
            SysRoleMenuEntity roleMenuEntity = new SysRoleMenuEntity();
            roleMenuEntity.setId(IdWorker.getIdStr());
            roleMenuEntity.setRoleId(form.getRoleId());
            roleMenuEntity.setMenuId(sysMenuEntity.getId());
            roleMenuEntities.add(roleMenuEntity);
        });
        this.saveBatch(roleMenuEntities);
    }

    @Override
    public Set<String> getMenuIdsByRoleId(String roleId) {
        return this.lambdaQuery()
                .eq(SysRoleMenuEntity::getRoleId,roleId)
                .list()
                .stream()
                .map(SysRoleMenuEntity::getMenuId)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<String> getMenuIdsByRoleIds(Collection<String> roleIds) {
        return this.lambdaQuery()
                .in(SysRoleMenuEntity::getRoleId,roleIds)
                .list()
                .stream()
                .map(SysRoleMenuEntity::getMenuId)
                .collect(Collectors.toSet());
    }

    private Map<String,String> menuMappingParentMap(){
        Map<String,String> menuMappingParentMap = Maps.newHashMap();
        List<SysMenuEntity> allMenuList = sysMenuCacheService.getAllMenuList();
        if(CollUtil.isNotEmpty(allMenuList)){
            allMenuList.stream().forEach(sysMenuEntity -> menuMappingParentMap.put(sysMenuEntity.getId(),sysMenuEntity.getParentId()));
        }
        return menuMappingParentMap;
    }

    private void getCurrentMenuUpperIds(Set<String> upperIds,Map<String,String> menuMappingParentMap,String menuId){
        if(Objects.isNull(upperIds)){
            upperIds = Sets.newHashSet();
        }
        String parentId = menuMappingParentMap.get(menuId);
        upperIds.add(parentId);
        if(!SysMenuEntity.ROOT_PARENT.equals(parentId)){
            getCurrentMenuUpperIds(upperIds,menuMappingParentMap,parentId);
        }
    }

}
