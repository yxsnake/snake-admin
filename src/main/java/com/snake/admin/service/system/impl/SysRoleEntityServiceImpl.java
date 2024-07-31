package com.snake.admin.service.system.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.snake.admin.mapper.system.SysRoleEntityMapper;
import com.snake.admin.model.system.entity.SysRoleEntity;
import com.snake.admin.service.system.SysRoleEntityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SysRoleEntityServiceImpl extends ServiceImpl<SysRoleEntityMapper, SysRoleEntity> implements SysRoleEntityService {
}
