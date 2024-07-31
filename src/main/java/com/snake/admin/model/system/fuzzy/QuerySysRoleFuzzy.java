package com.snake.admin.model.system.fuzzy;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "角色查询条件(模糊查询)")
public class QuerySysRoleFuzzy {

    private String name;

    private String code;
}
