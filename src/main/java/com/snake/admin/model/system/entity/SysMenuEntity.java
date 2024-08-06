package com.snake.admin.model.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.github.yxsnake.pisces.web.core.base.BaseEntity;
import io.github.yxsnake.pisces.web.core.converter.Convert;
import lombok.Data;

import java.util.Arrays;
import java.util.List;

@Data
@TableName(value = "sys_menu")
public class SysMenuEntity extends BaseEntity implements Convert {

    public final static String ROOT_PARENT = "0";

    public final static List<String> NOT_ALLOW_DELETE_MENU_LIST = Arrays.asList(
            "/system",
            "/system/user/index",
            "/system/role/index",
            "/system/menu/index",
            "/system/dept/index"
    );

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

    private Integer frameLoading;

    private Integer keepAlive;

    private Integer hiddenTag;

    private Integer fixedTag;

    private Integer showLink;

    private Integer showParent;

}
