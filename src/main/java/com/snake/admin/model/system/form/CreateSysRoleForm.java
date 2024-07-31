package com.snake.admin.model.system.form;

import io.github.yxsnake.pisces.web.core.converter.Convert;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Data
@Schema(name = "创建角色表单")
public class CreateSysRoleForm implements Convert {

    @Schema(description = "角色名称")
    @NotBlank(message = "")
    private String name;

    @Schema(description = "角色标识")
    @NotBlank(message = "")
    private String code;

    @Schema(description = "备注")
    private String remark;
}
