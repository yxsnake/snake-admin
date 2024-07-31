package com.snake.admin.model.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.github.yxsnake.pisces.web.core.base.BaseEntity;
import io.github.yxsnake.pisces.web.core.converter.Convert;
import lombok.Data;


@Data
@TableName(value = "sys_dept")
public class SysDeptEntity extends BaseEntity implements Convert {

    @TableId(type = IdType.NONE)
    private String id;

    private String parentId;

    private String name;

    private String phone;

    private String principal;

    private String email;

    private Integer type;

    private Integer status;

    private String remark;

    private Long sort;

}
