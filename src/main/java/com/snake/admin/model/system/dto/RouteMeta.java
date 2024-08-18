package com.snake.admin.model.system.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Set;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Data
@Schema(name = "路由元数据信息")
public class RouteMeta {

    @Schema(description = "菜单图标")
    private String icon;

    @Schema(description = "菜单名称")
    private String title;

    @Schema(description = "排序")
    @JsonInclude(NON_NULL)
    private Long rank;

    private Boolean showLink = true;

    @Schema(description = "当前路由具有权限的角色标识集合")
    private Set<String> roles;

    @Schema(description = "当前按钮具备可用的权限标识集合")
    private Set<String> auths;

    private String frameSrc;

    private Boolean keepAlive = false;

}
