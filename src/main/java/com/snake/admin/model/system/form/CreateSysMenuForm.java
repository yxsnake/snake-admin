package com.snake.admin.model.system.form;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.snake.admin.common.enums.AnimationEnum;
import io.github.yxsnake.pisces.web.core.converter.Convert;
import io.github.yxsnake.pisces.web.core.utils.BizAssert;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


@Data
@Schema(name = "创建菜单表单")
public class CreateSysMenuForm implements Convert {

    @Schema(description = "菜单类型")
    private Integer menuType;

    @Schema(description = "上级菜单")
    private String parentId;

    @Schema(description = "路由路径")
    private String path;

    @Schema(description = "路由名称")
    private String name;

    @Schema(description = "菜单名称")
    private String title;

    @Schema(description = "组件路径")
    private String component;

    @Schema(description = "菜单排序")
    private Long rank = DateUtil.date().getTime();

    @Schema(description = "路由重定向")
    private String redirect;

    @Schema(description = "菜单图标")
    private String icon;

    @Schema(description = "右侧图标")
    private String extraIcon;

    @Schema(description = "进场动画")
    private String enterTransition = AnimationEnum.BOUNCE.getValue() ;

    @Schema(description = "离场动画")
    private String leaveTransition = AnimationEnum.BOUNCE.getValue();

    @Schema(description = "需要激活的菜单")
    private String activePath;

    @Schema(description = "是否显示该菜单")
    private Integer showLink = 1;

    @Schema(description = "是否显示父级菜单")
    private Integer showParent = 0;

    @Schema(description = "是否缓存页面")
    private Integer keepAlive = 0;

    @Schema(description = "当前菜单名称或自定义信息禁止添加到标签页")
    private Integer hiddenTag = 1;

    @Schema(description = "当前菜单名称是否固定显示在标签页且不可关闭")
    private Integer fixedTag  = 0;

    @Schema(description = "加载动画（0-false;1-true,内嵌的`iframe`页面是否开启首次加载动画）")
    private Integer frameLoading = 1;

    @Schema(description = "链接地址（需要内嵌的`iframe`链接地址）")
    private String frameSrc;

    @Schema(description = "权限标识")
    private String auths;


    /**
     * 菜单 类型校验
     */
    public void checkMenuParam(){
        BizAssert.isTrue("菜单名称不能为空", StrUtil.isBlank(title));
        BizAssert.isTrue("路由名称不能为空", StrUtil.isBlank(name));
        BizAssert.isTrue("路由路径不能为空", StrUtil.isBlank(path));
    }

    /**
     * iframe 类型校验
     */
    public void checkIFrameParam(){
        BizAssert.isTrue("菜单名称不能为空", StrUtil.isBlank(title));
        BizAssert.isTrue("路由名称不能为空", StrUtil.isBlank(name));
        BizAssert.isTrue("路由路径不能为空", StrUtil.isBlank(path));
        BizAssert.isTrue("链接地址不能为空",StrUtil.isBlank(frameSrc));
    }

    /**
     * 外链  类型校验
     */
    public void checkLinkParam(){
        BizAssert.isTrue("菜单名称不能为空", StrUtil.isBlank(title));
        BizAssert.isTrue("路由名称不能为空", StrUtil.isBlank(name));
        BizAssert.isTrue("路由路径不能为空", StrUtil.isBlank(path));
    }

    /**
     * 按钮 类型校验
     */
    public void checkButtonParam(){
        BizAssert.isTrue("菜单名称不能为空", StrUtil.isBlank(title));
        BizAssert.isTrue("权限标识不能为空",StrUtil.isBlank(auths));
    }

}
