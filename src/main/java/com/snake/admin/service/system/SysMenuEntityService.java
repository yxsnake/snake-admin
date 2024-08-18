package com.snake.admin.service.system;

import com.baomidou.mybatisplus.extension.service.IService;
import com.snake.admin.model.system.dto.SysMenuDTO;
import com.snake.admin.model.system.entity.SysMenuEntity;
import com.snake.admin.model.system.form.CreateSysMenuForm;
import com.snake.admin.model.system.form.ModifySysMenuForm;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface SysMenuEntityService extends IService<SysMenuEntity> {

    void create(CreateSysMenuForm form);

    void modify(ModifySysMenuForm form);

    void deleteById(String id);

    SysMenuDTO detail(String id);

    List<SysMenuEntity> getMenuListByUserId(String userId,Boolean containsAdminRole);

    List<SysMenuDTO> queryList();

    Map<String, Set<String>> getMenuSubButtonPermsMap();
}
