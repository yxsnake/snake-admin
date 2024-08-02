package com.snake.admin.model.system.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
@Schema(name = "角色授权菜单表单")
public class AuthorizedSysRoleMenuForm {

    @Schema(description = "角色ID")
    @NotBlank(message = "角色不能为空")
    private String roleId;

    @Schema(description = "菜单集合")
    @NotEmpty(message = "请至少选择一个菜单")
    private List<String> menuIds;
}
