package com.snake.admin.mapper.system;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.snake.admin.model.system.entity.SysMenuEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SysMenuEntityMapper extends BaseMapper<SysMenuEntity> {

    List<SysMenuEntity> selectMenuListByUserId(String userId);
}
