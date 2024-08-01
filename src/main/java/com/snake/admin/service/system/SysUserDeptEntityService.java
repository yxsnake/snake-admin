package com.snake.admin.service.system;

import com.baomidou.mybatisplus.extension.service.IService;
import com.snake.admin.model.system.entity.SysUserDeptEntity;

/**
 * @author: snake
 * @create-time: 2024-08-01
 * @description:
 * @version: 1.0
 */
public interface SysUserDeptEntityService extends IService<SysUserDeptEntity> {
    /**
     * 查询当前部门下是否存在用户
     * @param deptId
     * @return
     */
    Boolean existsUser(String deptId);
}
