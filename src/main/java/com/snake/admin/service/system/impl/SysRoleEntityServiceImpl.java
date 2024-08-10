package com.snake.admin.service.system.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.snake.admin.cache.system.SysRoleCacheService;
import com.snake.admin.cache.system.SysUserRoleCacheService;
import com.snake.admin.common.enums.SysRoleStatusEnum;
import com.snake.admin.mapper.system.SysRoleEntityMapper;
import com.snake.admin.model.system.dto.SysRoleDTO;
import com.snake.admin.model.system.entity.SysRoleEntity;
import com.snake.admin.model.system.equal.QuerySysRoleEqual;
import com.snake.admin.model.system.form.CreateSysRoleForm;
import com.snake.admin.model.system.form.ModifySysRoleForm;
import com.snake.admin.model.system.form.UpdateSysRoleStatusForm;
import com.snake.admin.model.system.fuzzy.QuerySysRoleFuzzy;
import com.snake.admin.service.system.SysRoleEntityService;
import com.snake.admin.service.system.SysUserRoleEntityService;
import io.github.yxsnake.pisces.web.core.base.QueryFilter;
import io.github.yxsnake.pisces.web.core.utils.BizAssert;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SysRoleEntityServiceImpl extends ServiceImpl<SysRoleEntityMapper, SysRoleEntity> implements SysRoleEntityService {

    private final SysUserRoleEntityService sysUserRoleEntityService;

    private final SysRoleCacheService sysRoleCacheService;

    private final SysUserRoleCacheService sysUserRoleCacheService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(CreateSysRoleForm form) {
        String id = IdWorker.getIdStr();
        SysRoleEntity sysRoleEntity = form.convert(SysRoleEntity.class);
        sysRoleEntity.setId(id);
        if(Objects.isNull(sysRoleEntity.getStatus())){
            sysRoleEntity.setStatus(SysRoleStatusEnum.NORMAL.getValue());
        }
        this.save(sysRoleEntity);
        sysRoleCacheService.writeRoleToCache(sysRoleEntity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void modify(ModifySysRoleForm form) {
        String id = form.getId();
        SysRoleEntity sysRoleEntity = this.getBaseMapper().selectById(id);
        BizAssert.isTrue("角色信息不存在",Objects.isNull(sysRoleEntity));
        BeanUtils.copyProperties(form,sysRoleEntity);
        this.getBaseMapper().updateById(sysRoleEntity);
        // 写入缓存
        SysRoleEntity roleEntity = this.getBaseMapper().selectById(id);
        sysRoleCacheService.writeRoleToCache(roleEntity);
    }

    @Override
    public SysRoleDTO detail(String id) {
        SysRoleEntity roleEntity = sysRoleCacheService.readRoleFormCache(id);
        if(Objects.nonNull(roleEntity)){
            return roleEntity.convert(SysRoleDTO.class);
        }
        SysRoleEntity sysRoleEntity = this.getBaseMapper().selectById(id);
        if(Objects.isNull(sysRoleEntity)){
            return null;
        }
        return sysRoleEntity.convert(SysRoleDTO.class);
    }

    @Override
    public IPage<SysRoleDTO> pageList(QueryFilter<QuerySysRoleEqual, QuerySysRoleFuzzy> queryFilter) {
        QuerySysRoleEqual equalsQueries = QueryFilter.getEqualsQueries(QuerySysRoleEqual.class, queryFilter.getEqualsQueries());
        QuerySysRoleFuzzy fuzzyQueries = QueryFilter.getFuzzyQueries(QuerySysRoleFuzzy.class, queryFilter.getFuzzyQueries());
        return this.page(new Page<>(queryFilter.getPageNum(),queryFilter.getPageSize()),
                Wrappers.lambdaQuery(SysRoleEntity.class)
                        .eq(Objects.nonNull(equalsQueries.getStatus()),SysRoleEntity::getStatus,equalsQueries.getStatus())
                        .ne(SysRoleEntity::getCode,SysRoleEntity.ROLE_CODE_ADMIN)
                        .like(StrUtil.isNotBlank(fuzzyQueries.getName()),SysRoleEntity::getName,fuzzyQueries.getName())
                        .like(StrUtil.isNotBlank(fuzzyQueries.getCode()),SysRoleEntity::getCode,fuzzyQueries.getCode())
        ).convert(item->item.convert(SysRoleDTO.class));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(String id) {
        // 查询当前角色是否已分配给用户，如果已分配则不允许删除
        Boolean existBindUser = sysUserRoleEntityService.existRoleBindUser(id);
        BizAssert.isTrue("当前角色已绑定用户,不允许删除",existBindUser);
        SysRoleEntity sysRoleEntity = this.getBaseMapper().selectById(id);
        BizAssert.isTrue("角色不存在",Objects.isNull(sysRoleEntity));
        BizAssert.isTrue("管理员角色不允许删除",SysRoleEntity.ROLE_CODE_ADMIN.equals(sysRoleEntity.getCode()));
        this.getBaseMapper().deleteById(id);
        sysRoleCacheService.removeRoleCache(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateStatus(UpdateSysRoleStatusForm form) {
        String id = form.getId();
        SysRoleEntity sysRoleEntity = this.getBaseMapper().selectById(id);
        BizAssert.isTrue("角色信息不存在", Objects.isNull(sysRoleEntity));
        sysRoleEntity.setStatus(form.getStatus());
        this.getBaseMapper().updateById(sysRoleEntity);
        SysRoleEntity roleEntity = this.getBaseMapper().selectById(id);
        sysRoleCacheService.writeRoleToCache(roleEntity);
    }

    @Override
    public List<SysRoleDTO> getAllRoleList() {
        List<SysRoleEntity> roles = sysRoleCacheService.getAllRoleList();
        if(CollUtil.isNotEmpty(roles)){
            return roles.stream().map(sysRoleEntity -> sysRoleEntity.convert(SysRoleDTO.class)).collect(Collectors.toList());
        }
        return this.lambdaQuery().list()
                .stream()
                .map(sysRoleEntity -> sysRoleEntity.convert(SysRoleDTO.class))
                .collect(Collectors.toList());
    }

    /**
     * 当前用户是否包含 超级管理员角色
     * @param userId
     * @return
     */
    public Boolean containsAdminRole(String userId) {
        Set<String> roleIds = sysUserRoleCacheService.readUserRoles(userId);
        List<SysRoleEntity> sysRoleEntities = sysRoleCacheService.readRoleFormCache(roleIds);
        return sysRoleEntities.stream().filter(sysRoleEntity -> SysRoleEntity.ROLE_CODE_ADMIN.equals(sysRoleEntity.getCode())).count()>0;
    }
}
