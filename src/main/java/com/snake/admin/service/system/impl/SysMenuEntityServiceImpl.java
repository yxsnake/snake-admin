package com.snake.admin.service.system.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.snake.admin.mapper.system.SysMenuEntityMapper;
import com.snake.admin.model.system.entity.SysMenuEntity;
import com.snake.admin.service.system.SysMenuEntityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SysMenuEntityServiceImpl extends ServiceImpl<SysMenuEntityMapper, SysMenuEntity> implements SysMenuEntityService {
}
