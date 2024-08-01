package com.snake.admin.service.system.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.snake.admin.mapper.system.SysUserDeptEntityMapper;
import com.snake.admin.model.system.entity.SysUserDeptEntity;
import com.snake.admin.service.system.SysUserDeptEntityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author: snake
 * @create-time: 2024-08-01
 * @description:
 * @version: 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SysUserDeptEntityServiceImpl extends ServiceImpl<SysUserDeptEntityMapper, SysUserDeptEntity> implements SysUserDeptEntityService {
    @Override
    public Boolean existsUser(String deptId) {
        return this.lambdaQuery().eq(SysUserDeptEntity::getDeptId,deptId).list().stream().count() > 0;
    }
}
