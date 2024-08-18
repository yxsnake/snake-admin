package com.snake.admin.model.system.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.Set;

@Data
@Schema(name = "登录响应结果")
public class LoginSysUserDTO {

    @Schema(description = "访问令牌")
    private String accessToken;

    @Schema(description = "刷新令牌")
    private String refreshToken;

    @Schema(description = "头像")
    private String avatar;

    @Schema(description = "过期时间")
    @DateTimeFormat(pattern = "yyyy/MM/dd HH:mm:ss")
    private Date expires;

    @Schema(description = "性别")
    private Integer gender;

    @Schema(description = "昵称")
    private String name;

    @Schema(description = "账号状态")
    private Integer status;

    @Schema(description = "用户ID")
    private String userId;

    @Schema(description = "账号")
    private String username;

    private String nickname;

    @Schema(description = "拥有的角色标识集合")
    private Set<String> roles;
}
