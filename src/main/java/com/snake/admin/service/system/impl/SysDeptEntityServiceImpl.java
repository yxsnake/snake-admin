package com.snake.admin.service.system.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.snake.admin.mapper.system.SysDeptEntityMapper;
import com.snake.admin.model.system.entity.SysDeptEntity;
import com.snake.admin.service.system.SysDeptEntityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SysDeptEntityServiceImpl extends ServiceImpl<SysDeptEntityMapper, SysDeptEntity> implements SysDeptEntityService {
}
