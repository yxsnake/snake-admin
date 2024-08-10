package com.snake.admin.service.system.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.snake.admin.cache.system.SysRoleMenuCacheService;
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

import java.util.List;
import java.util.Set;
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

    private final SysRoleMenuCacheService sysRoleMenuCacheService;
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
        // 对前端传递参数进行去重
        Set<String> menuIds = Sets.newHashSet();
        menuIds.addAll(form.getMenuIds());
        // 反向查询角色信息，防止 不存在的角色
        List<SysMenuEntity> sysMenuEntities = sysMenuEntityMapper.selectBatchIds(menuIds);
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
        // 写入缓存
        if(CollUtil.isNotEmpty(oldMenuIds)){
            sysRoleMenuCacheService.removeRoleMenuIds(form.getRoleId(),oldMenuIds);
        }
        sysRoleMenuCacheService.writeRoleMenuIdsCache(form.getRoleId(),menuIds);
    }

    @Override
    public Set<String> getMenuIdsByRoleId(String roleId) {
        return sysRoleMenuCacheService.readRoleMenuIdsCache(roleId);
    }
}
