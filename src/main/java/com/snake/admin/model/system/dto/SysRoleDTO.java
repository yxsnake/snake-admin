package com.snake.admin.model.system.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "角色信息")
public class SysRoleDTO {

    @Schema(description = "角色ID")
    private String id;

    @Schema(description = "角色名称")
    private String name;

    @Schema(description = "角色编码")
    private String code;

    @Schema(description = "角色状态")
    private Integer status;

    @Schema(description = "备注")
    private String remark;
}
