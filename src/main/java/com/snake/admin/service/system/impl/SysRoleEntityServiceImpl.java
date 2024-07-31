package com.snake.admin.service.system.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.snake.admin.mapper.system.SysRoleEntityMapper;
import com.snake.admin.model.system.dto.SysRoleDTO;
import com.snake.admin.model.system.entity.SysRoleEntity;
import com.snake.admin.model.system.equal.QuerySysRoleEqual;
import com.snake.admin.model.system.form.CreateSysRoleForm;
import com.snake.admin.model.system.form.ModifySysRoleForm;
import com.snake.admin.model.system.form.UpdateSysRoleStatusForm;
import com.snake.admin.model.system.fuzzy.QuerySysRoleFuzzy;
import com.snake.admin.service.system.SysRoleEntityService;
import io.github.yxsnake.pisces.web.core.base.QueryFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SysRoleEntityServiceImpl extends ServiceImpl<SysRoleEntityMapper, SysRoleEntity> implements SysRoleEntityService {
    @Override
    public void create(CreateSysRoleForm form) {

    }

    @Override
    public void modify(ModifySysRoleForm form) {

    }

    @Override
    public SysRoleDTO detail(String id) {
        return null;
    }

    @Override
    public IPage<SysRoleDTO> pageList(QueryFilter<QuerySysRoleEqual, QuerySysRoleFuzzy> queryFilter) {
        return null;
    }

    @Override
    public void deleteById(String id) {

    }

    @Override
    public void updateStatus(UpdateSysRoleStatusForm form) {

    }
}
