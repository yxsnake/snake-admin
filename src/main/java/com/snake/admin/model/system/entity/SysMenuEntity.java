package com.snake.admin.model.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.github.yxsnake.pisces.web.core.base.BaseEntity;
import io.github.yxsnake.pisces.web.core.converter.Convert;
import io.github.yxsnake.pisces.web.core.utils.JsonUtils;
import lombok.Data;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

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

    private Boolean frameLoading;

    private Boolean keepAlive;

    private Boolean hiddenTag;

    private Boolean fixedTag;

    private Boolean showLink;

    private Boolean showParent;

    @Override
    public String toString(){
        return JsonUtils.objectCovertToJson(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        SysMenuEntity that = (SysMenuEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(parentId, that.parentId) && Objects.equals(menuType, that.menuType) && Objects.equals(title, that.title) && Objects.equals(name, that.name) && Objects.equals(path, that.path) && Objects.equals(component, that.component) && Objects.equals(rank, that.rank) && Objects.equals(redirect, that.redirect) && Objects.equals(icon, that.icon) && Objects.equals(extraIcon, that.extraIcon) && Objects.equals(enterTransition, that.enterTransition) && Objects.equals(leaveTransition, that.leaveTransition) && Objects.equals(activePath, that.activePath) && Objects.equals(auths, that.auths) && Objects.equals(frameSrc, that.frameSrc) && Objects.equals(frameLoading, that.frameLoading) && Objects.equals(keepAlive, that.keepAlive) && Objects.equals(hiddenTag, that.hiddenTag) && Objects.equals(fixedTag, that.fixedTag) && Objects.equals(showLink, that.showLink) && Objects.equals(showParent, that.showParent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, parentId, menuType, title, name, path, component, rank, redirect, icon, extraIcon, enterTransition, leaveTransition, activePath, auths, frameSrc, frameLoading, keepAlive, hiddenTag, fixedTag, showLink, showParent);
    }
}
