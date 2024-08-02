package com.snake.admin.model.system.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
@Schema(name = "用户授权角色表单")
public class AuthorizedSysUserRoleForm {

    @Schema(description = "用户ID")
    @NotBlank(message = "用户不能为空")
    private String userId;

    @Schema(description = "角色集合")
    @NotEmpty(message = "请至少选择一个角色")
    private List<String> roleIds;
}
