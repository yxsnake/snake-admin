package com.snake.admin.model.system.equal;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "用户查询条件(等值查询)")
public class QuerySysUserEqual {

    private Integer status;

    private String deptId;

}
