package com.snake.admin.model.system.form;

import io.github.yxsnake.pisces.web.core.converter.Convert;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Data
@Schema(name = "创建部门表单")
public class CreateSysDeptForm implements Convert {

    @Schema(description = "上级部门")
    private String parentId;

    @Schema(description = "部门名称")
    @NotBlank(message = "name不能为空")
    private String name;

    @Schema(description = "手机号")
    private String phone;

    @Schema(description = "部门负责人")
    private String principal;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "排序")
    private Long sort;

    @Schema(description = "备注")
    private String remark;
}
