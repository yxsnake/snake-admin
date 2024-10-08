package com.snake.admin.model.system.form;

import io.github.yxsnake.pisces.web.core.converter.Convert;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(name = "修改用户表单")
public class ModifySysUserForm implements Convert {

    @Schema(description = "用户ID")
    @NotBlank(message = "请传递用户ID")
    private String id;

    @Schema(description = "用户昵称")
    @NotBlank(message = "请填写用户昵称")
    private String nickname;

    @Schema(description = "用户名称")
    @NotBlank(message = "请填写用户名称")
    private String username;

    @Schema(description = "手机号")
    private String phone;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "用户性别")
    private Integer sex;

    @Schema(description = "归属部门")
    @NotBlank(message = "请选择归属部门")
    private String deptId;

    @Schema(description = "用户状态")
    private Integer status;

    @Schema(description = "备注")
    private String remark;
}
