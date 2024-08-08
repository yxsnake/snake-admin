package com.snake.admin.common.enums;

import io.github.yxsnake.pisces.web.core.base.IBaseEnum;
import lombok.Getter;

import java.util.Arrays;

/**
 * @author: snake
 * @create-time: 2024-08-08
 * @description:
 * @version: 1.0
 */
@Getter
public enum SysUserDeletedEnum implements IBaseEnum<Integer> {

    DELETED(1,"删除"),

    NORMAL(0,"正常"),

    ;

    private final Integer value;

    private final String label;

    SysUserDeletedEnum(final Integer value, final String label){
        this.value = value;
        this.label = label;
    }

    public static SysUserDeletedEnum getInstance(final Integer value){
        return Arrays.asList(values()).stream().filter(item->item.getValue().equals(value)).findFirst().orElse(null);
    }
}
