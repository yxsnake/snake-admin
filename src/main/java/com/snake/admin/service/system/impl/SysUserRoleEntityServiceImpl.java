package com.snake.admin.service.system.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.snake.admin.mapper.system.SysUserRoleEntityMapper;
import com.snake.admin.model.system.entity.SysUserRoleEntity;
import com.snake.admin.service.system.SysUserRoleEntityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SysUserRoleEntityServiceImpl extends ServiceImpl<SysUserRoleEntityMapper, SysUserRoleEntity> implements SysUserRoleEntityService {
}
