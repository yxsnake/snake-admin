package com.snake.admin.model.system.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "部门信息")
public class SysDeptDTO {

    @Schema(description = "部门ID")
    private String id;

    @Schema(description = "上级部门ID")
    private String parentId;

    @Schema(description = "部门名称")
    private String name;

    @Schema(description = "部门负责人电话")
    private String phone;

    @Schema(description = "部门负责人")
    private String principal;

    @Schema(description = "部门负责人邮箱")
    private String email;

    @Schema(description = "部门类型")
    private Integer type;

    @Schema(description = "部门状态")
    private Integer status;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "排序")
    private Long sort;

}
