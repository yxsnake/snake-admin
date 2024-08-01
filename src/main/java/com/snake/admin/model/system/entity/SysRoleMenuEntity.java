package com.snake.admin.model.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.github.yxsnake.pisces.web.core.base.BaseEntity;
import io.github.yxsnake.pisces.web.core.converter.Convert;
import lombok.Data;

/**
 * @author: snake
 * @create-time: 2024-08-01
 * @description:
 * @version: 1.0
 */
@Data
@TableName(value = "sys_role_menu")
public class SysRoleMenuEntity extends BaseEntity implements Convert {

    @TableId(type = IdType.NONE)
    private String id;

    private String roleId;

    private String menuId;
}
