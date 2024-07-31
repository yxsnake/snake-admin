package com.snake.admin.model.system.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "用户信息")
public class SysUserDTO {

    @Schema(description = "用户ID")
    private String id;

    @Schema(description = "登录账号")
    private String username;

    @Schema(description = "昵称")
    private String nickname;

    @Schema(description = "电话")
    private String phone;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "头像")
    private String avatar;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "性别")
    private Integer sex;

    @Schema(description = "账号状态")
    private Integer status;
}
