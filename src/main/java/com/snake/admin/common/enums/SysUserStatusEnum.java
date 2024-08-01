package com.snake.admin.common.enums;

import io.github.yxsnake.pisces.web.core.base.IBaseEnum;
import lombok.Getter;

import java.util.Arrays;

/**
 * @author: snake
 * @create-time: 2024-08-01
 * @description: 系统用户状态
 * @version: 1.0
 */
@Getter
public enum SysUserStatusEnum implements IBaseEnum<Integer> {

    NORMAL(1,"正常"),

    DISABLE(0,"禁用"),

    ;

    private final Integer value;

    private final String label;

    SysUserStatusEnum(final Integer value, final String label){
        this.value = value;
        this.label = label;
    }

    public static SysUserStatusEnum getInstance(final Integer value){
        return Arrays.asList(values()).stream().filter(item->item.getValue().equals(value)).findFirst().orElse(null);
    }
}
