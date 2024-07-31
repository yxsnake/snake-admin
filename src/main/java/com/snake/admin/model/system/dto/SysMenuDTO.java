package com.snake.admin.model.system.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "菜单信息")
public class SysMenuDTO {

    @Schema(description = "菜单ID")
    private String id;

    @Schema(description = "上级菜单ID")
    private String parentId;

    @Schema(description = "菜单类型（`0`代表菜单、`1`代表`iframe`、`2`代表外链、`3`代表按钮）")
    private Integer menuType;

    @Schema(description = "菜单名称（兼容国际化、非国际化，如果用国际化的写法就必须在根目录的`locales`文件夹下对应添加）")
    private String title;

    @Schema(description = "路由名称（必须唯一并且和当前路由`component`字段对应的页面里用`defineOptions`包起来的`name`保持一致）")
    private String name;

    @Schema(description = "路由路径")
    private String path;

    @Schema(description = "组件路径（传`component`组件路径，那么`path`可以随便写，如果不传，`component`组件路径会跟`path`保持一致）")
    private String component;

    @Schema(description = "菜单排序（平台规定只有`home`路由的`rank`才能为`0`，所以后端在返回`rank`的时候需要从非`0`开始")
    private Long rank;

    @Schema(description = "路由重定向")
    private String redirect;

    @Schema(description = "菜单图标")
    private String icon;

    @Schema(description = "右侧图标")
    private String extraIcon;

    @Schema(description = "进场动画（页面加载动画）")
    private String enterTransition;

    @Schema(description = "离场动画（页面加载动画）")
    private String leaveTransition;

    @Schema(description = "菜单激活（将某个菜单激活，主要用于通过`query`或`params`传参的路由，当它们通过配置`showLink: false`后不在菜单中显示，就不会有任何菜单高亮，而通过设置`activePath`指定激活菜单即可获得高亮，`activePath`为指定激活菜单的`path`）")
    private String activePath;

    @Schema(description = "权限标识（按钮级别权限设置）")
    private String auths;

    @Schema(description = "链接地址（需要内嵌的`iframe`链接地址）")
    private String frameSrc;

    @Schema(description = "加载动画（0-false;1-true,内嵌的`iframe`页面是否开启首次加载动画）")
    private Boolean frameLoading;

    @Schema(description = "缓存页面（0-false;1-true,是否缓存该路由页面，开启后会保存该页面的整体状态，刷新后会清空状态）")
    private Boolean keepAlive;

    @Schema(description = "标签页（0-false;1-true,当前菜单名称或自定义信息禁止添加到标签页）")
    private Boolean hiddenTag;

    @Schema(description = "固定标签页（0-false;1-true,当前菜单名称是否固定显示在标签页且不可关闭）")
    private Boolean fixedTag;

    @Schema(description = "是否显示该菜单（0-false;1-true）")
    private Boolean showLink;

    @Schema(description = "是否显示父级菜单(0-false;1-true)")
    private Boolean showParent;

}
