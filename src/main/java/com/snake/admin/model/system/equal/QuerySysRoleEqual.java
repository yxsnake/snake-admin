package com.snake.admin.model.system.equal;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "角色查询条件(等值查询)")
public class QuerySysRoleEqual {

    private Integer status;
}
