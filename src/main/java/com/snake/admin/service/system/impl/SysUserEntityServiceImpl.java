package com.snake.admin.service.system.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.snake.admin.mapper.system.SysUserEntityMapper;
import com.snake.admin.model.system.entity.SysUserEntity;
import com.snake.admin.service.system.SysUserEntityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SysUserEntityServiceImpl extends ServiceImpl<SysUserEntityMapper, SysUserEntity> implements SysUserEntityService {

}
