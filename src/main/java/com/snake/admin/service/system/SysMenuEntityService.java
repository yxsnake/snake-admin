package com.snake.admin.service.system;

import com.baomidou.mybatisplus.extension.service.IService;
import com.snake.admin.model.system.dto.RouteMenuDTO;
import com.snake.admin.model.system.dto.SysMenuDTO;
import com.snake.admin.model.system.entity.SysMenuEntity;
import com.snake.admin.model.system.form.CreateSysMenuForm;
import com.snake.admin.model.system.form.ModifySysMenuForm;

import java.util.List;

public interface SysMenuEntityService extends IService<SysMenuEntity> {

    List<SysMenuDTO> listByUserId(String userId);

    void create(CreateSysMenuForm form);

    void modify(ModifySysMenuForm form);

    void deleteById(String id);

    SysMenuDTO detail(String id);

    List<RouteMenuDTO> getCurrentUserRoutes(String s);

    List<SysMenuEntity> getCurrentMenuIds(String userId,Boolean containsAdminRole);

    List<SysMenuDTO> queryList();
}
