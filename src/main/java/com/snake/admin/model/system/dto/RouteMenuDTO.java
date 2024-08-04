package com.snake.admin.model.system.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

/**
 * @author: snake
 * @create-time: 2024-08-03
 * @description:
 * @version: 1.0
 */
@Data
@Schema(name = "菜单信息")
public class RouteMenuDTO {

    @Schema(description = "菜单ID")
    @JsonIgnore
    private String id;

    @Schema(description = "上级菜单ID")
    @JsonIgnore
    private String parentId;

    @Schema(description = "菜单类型（`0`代表菜单、`1`代表`iframe`、`2`代表外链、`3`代表按钮）")
    @JsonIgnore
    private Integer menuType;

    @Schema(description = "路由地址")
    private String path;

    @Schema(description = "路由名称")
    private String name;

    @Schema(description = "组件名称")
    @JsonInclude(NON_EMPTY)
    private String component;

    @Schema(description = "路由元数据信息")
    private RouteMenuMetaDTO meta;

    @JsonInclude(NON_EMPTY)
    private List<RouteMenuDTO> children;
}
