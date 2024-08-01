package com.snake.admin.service.system.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.snake.admin.mapper.system.SysRoleMenuEntityMapper;
import com.snake.admin.model.system.entity.SysRoleMenuEntity;
import com.snake.admin.service.system.SysRoleMenuEntityService;
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
public class SysRoleMenuEntityServiceImpl extends ServiceImpl<SysRoleMenuEntityMapper, SysRoleMenuEntity> implements SysRoleMenuEntityService {
}
