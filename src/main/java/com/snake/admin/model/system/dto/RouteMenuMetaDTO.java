package com.snake.admin.model.system.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Set;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;
import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

/**
 * @author: snake
 * @create-time: 2024-08-03
 * @description:
 * @version: 1.0
 */
@Data
@Schema(name = "路由信息")
public class RouteMenuMetaDTO {

    @Schema(description = "菜单图标")
    private String icon;

    @Schema(description = "菜单名称")
    private String title;

    @Schema(description = "排序")
    @JsonInclude(NON_NULL)
    private Long rank;

    @Schema(description = "当前路由具有权限的角色标识集合")
    @JsonInclude(NON_EMPTY)
    private Set<String> roles;

    @Schema(description = "当前按钮具备可用的权限标识集合")
    @JsonInclude(NON_EMPTY)
    private Set<String> auths;

    @JsonInclude(NON_EMPTY)
    private String frameSrc;

    private Boolean keepAlive;

}
