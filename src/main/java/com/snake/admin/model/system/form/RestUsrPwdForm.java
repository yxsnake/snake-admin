package com.snake.admin.model.system.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(name = "重置密码")
public class RestUsrPwdForm {

    @Schema(description = "重置用户账号")
    @NotBlank(message = "账号不能为空")
    private String username;

    @Schema(description = "重置用户ID")
    @NotBlank(message = "请选择一个用户")
    private String userId;

    @Schema(description = "安全口令")
//    @NotBlank(message = "重置安全口令")
    private String safeCode;

}
