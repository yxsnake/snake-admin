package com.snake.admin.service.system;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.snake.admin.model.system.dto.SysUserDTO;
import com.snake.admin.model.system.entity.SysUserEntity;
import com.snake.admin.model.system.equal.QuerySysUserEqual;
import com.snake.admin.model.system.form.CreateSysUserForm;
import com.snake.admin.model.system.form.ModifySysUserForm;
import com.snake.admin.model.system.form.RestUsrPwdForm;
import com.snake.admin.model.system.form.UpdateSysUserStatusForm;
import com.snake.admin.model.system.fuzzy.QuerySysUserFuzzy;
import io.github.yxsnake.pisces.web.core.base.QueryFilter;

public interface SysUserEntityService extends IService<SysUserEntity> {

    void create(CreateSysUserForm form);

    void modify(ModifySysUserForm form);

    SysUserDTO detail(String id);

    void upateStatus(UpdateSysUserStatusForm form);

    IPage<SysUserDTO> pageList(QueryFilter<QuerySysUserEqual, QuerySysUserFuzzy> queryFilter);

    void restPassword(RestUsrPwdForm form);
}
