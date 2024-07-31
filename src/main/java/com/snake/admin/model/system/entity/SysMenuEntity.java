package com.snake.admin.model.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.github.yxsnake.pisces.web.core.base.BaseEntity;
import io.github.yxsnake.pisces.web.core.converter.Convert;
import lombok.Data;

@Data
@TableName(value = "sys_menu")
public class SysMenuEntity extends BaseEntity implements Convert {

    @TableId(type = IdType.NONE)
    private String id;

    private String parentId;

    private Integer menuType;

    private String title;

    private String name;

    private String path;

    private String component;

    private Long rank;

    private String redirect;

    private String icon;

    private String extraIcon;

    private String enterTransition;

    private String leaveTransition;

    private String activePath;

    private String auths;

    private String frameSrc;

    private Boolean frameLoading;

    private Boolean keepAlive;

    private Boolean hiddenTag;

    private Boolean fixedTag;

    private Boolean showLink;

    private Boolean showParent;

}
