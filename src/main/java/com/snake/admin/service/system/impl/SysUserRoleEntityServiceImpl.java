package com.snake.admin.service.system.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.snake.admin.common.enums.SysRoleStatusEnum;
import com.snake.admin.mapper.system.SysRoleEntityMapper;
import com.snake.admin.mapper.system.SysUserRoleEntityMapper;
import com.snake.admin.model.system.entity.SysRoleEntity;
import com.snake.admin.model.system.entity.SysUserRoleEntity;
import com.snake.admin.service.system.SysUserRoleEntityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
}
