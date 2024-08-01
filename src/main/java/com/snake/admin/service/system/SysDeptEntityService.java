package com.snake.admin.service.system;

import com.baomidou.mybatisplus.extension.service.IService;
import com.snake.admin.model.system.dto.SysDeptDTO;
import com.snake.admin.model.system.entity.SysDeptEntity;
import com.snake.admin.model.system.form.CreateSysDeptForm;
import com.snake.admin.model.system.form.ModifySysDeptForm;

public interface SysDeptEntityService extends IService<SysDeptEntity> {
    void create(CreateSysDeptForm form);

    void modify(ModifySysDeptForm form);

    SysDeptDTO detail(String id);

    void deleteById(String id);
}
