package com.snake.admin.service.system;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.snake.admin.model.system.dto.SysRoleDTO;
import com.snake.admin.model.system.entity.SysRoleEntity;
import com.snake.admin.model.system.equal.QuerySysRoleEqual;
import com.snake.admin.model.system.form.CreateSysRoleForm;
import com.snake.admin.model.system.form.ModifySysRoleForm;
import com.snake.admin.model.system.form.UpdateSysRoleStatusForm;
import com.snake.admin.model.system.fuzzy.QuerySysRoleFuzzy;
import io.github.yxsnake.pisces.web.core.base.QueryFilter;

import java.util.List;

public interface SysRoleEntityService extends IService<SysRoleEntity> {

    void create(CreateSysRoleForm form);

    void modify(ModifySysRoleForm form);

    SysRoleDTO detail(String id);

    IPage<SysRoleDTO> pageList(QueryFilter<QuerySysRoleEqual, QuerySysRoleFuzzy> queryFilter);

    void deleteById(String id);

    void updateStatus(UpdateSysRoleStatusForm form);

    List<SysRoleDTO> getAllRoleList();

    Boolean containsAdminRole(String userId);
}
