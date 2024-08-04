package com.snake.admin.model.system.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(name = "账号密码登录表单")
public class LoginSysUserForm {

    @Schema(description = "账号",requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "账号不允许为空")
    private String username;

    @Schema(description = "密码",requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "密码不允许为空")
    private String password;

}
