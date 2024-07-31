package com.snake.admin.model.system.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(name = "更新角色状态表单")
public class UpdateSysRoleStatusForm {

    @Schema(description = "角色ID")
    @NotBlank(message = "id不能为空")
    private String id;

    @Schema(description = "角色状态")
    @NotNull(message = "status不能为空")
    private Integer status;
}
