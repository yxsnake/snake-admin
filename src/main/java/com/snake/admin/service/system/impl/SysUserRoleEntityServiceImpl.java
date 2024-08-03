package com.snake.admin.service.system.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.snake.admin.common.enums.SysRoleStatusEnum;
import com.snake.admin.mapper.system.SysRoleEntityMapper;
import com.snake.admin.mapper.system.SysUserRoleEntityMapper;
import com.snake.admin.model.system.entity.SysRoleEntity;
import com.snake.admin.model.system.entity.SysUserRoleEntity;
import com.snake.admin.model.system.form.AuthorizedSysUserRoleForm;
import com.snake.admin.service.system.SysUserRoleEntityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SysUserRoleEntityServiceImpl extends ServiceImpl<SysUserRoleEntityMapper, SysUserRoleEntity> implements SysUserRoleEntityService {

    private final SysRoleEntityMapper sysRoleEntityMapper;

    @Override
    public Boolean existRoleBindUser(String id) {
        return this.lambdaQuery()
                .eq(SysUserRoleEntity::getRoleId,id)
                .list().stream().count()>0;
    }

    @Override
    public Set<String> getCurrentUserRoles(String userId) {
        Set<String> roleIds = this.lambdaQuery().eq(SysUserRoleEntity::getUserId, userId)
                .list().stream().map(SysUserRoleEntity::getRoleId).collect(Collectors.toSet());
        if(CollUtil.isEmpty(roleIds)){
            return null;
        }
        return sysRoleEntityMapper.selectList(
                Wrappers.lambdaQuery(SysRoleEntity.class)
                        .eq(SysRoleEntity::getStatus, SysRoleStatusEnum.NORMAL.getValue())
                        .in(SysRoleEntity::getId,roleIds)
        ).stream().map(SysRoleEntity::getCode).collect(Collectors.toSet());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void authorizedRole(AuthorizedSysUserRoleForm form) {
        List<SysUserRoleEntity> list = this.lambdaQuery().eq(SysUserRoleEntity::getUserId, form.getUserId()).list();
        if(CollUtil.isNotEmpty(list)){
            this.getBaseMapper().deleteBatchIds(list.stream().map(SysUserRoleEntity::getId).collect(Collectors.toSet()));
        }
        // 对前端传递参数进行去重
        Set<String> roleIds = Sets.newHashSet();
        roleIds.addAll(form.getRoleIds());
        // 反向查询角色信息，防止 不存在的角色
        List<SysRoleEntity> sysRoleEntities = sysRoleEntityMapper.selectBatchIds(roleIds);
        // 组装 用户角色关联关系对象
        List<SysUserRoleEntity> userRoleEntities = Lists.newArrayList();
        sysRoleEntities.stream().forEach(sysRoleEntity -> {
            SysUserRoleEntity userRoleEntity = new SysUserRoleEntity();
            userRoleEntity.setId(IdWorker.getIdStr());
            userRoleEntity.setUserId(form.getUserId());
            userRoleEntity.setRoleId(sysRoleEntity.getId());
            userRoleEntities.add(userRoleEntity);
        });
        this.saveBatch(userRoleEntities);
    }

    /**
     * 当前用户是否包含 超级管理员角色
     * @param userId
     * @return
     */
    @Override
    public Boolean containsAdminRole(String userId) {
        List<String> roleIds = this.lambdaQuery().eq(SysUserRoleEntity::getUserId, userId)
                .list().stream().map(SysUserRoleEntity::getRoleId).collect(Collectors.toList());
        if(CollUtil.isEmpty(roleIds)){
            return Boolean.FALSE;
        }
        List<String> roleCodes = sysRoleEntityMapper.selectList(
                Wrappers.lambdaQuery(SysRoleEntity.class)
                        .in(SysRoleEntity::getId, roleIds)
                )
                .stream().map(SysRoleEntity::getCode).collect(Collectors.toList());
        if(CollUtil.isEmpty(roleIds)){
            return Boolean.FALSE;
        }
        return roleCodes.contains(SysRoleEntity.ROLE_CODE_ADMIN);
    }
}
