package com.snake.admin.common.enums;

import lombok.Getter;

import java.util.Arrays;

/**
 * @author: snake
 * @create-time: 2024-08-01
 * @description:
 * @version: 1.0
 */
@Getter
public enum SysDeptStatusEnum {

    NORMAL(1,"正常"),

    DISABLE(0,"禁用"),

    ;

    private final Integer value;

    private final String label;

    SysDeptStatusEnum(final Integer value,final String label){
        this.value = value;
        this.label = label;
    }

    public static SysDeptStatusEnum getInstance(final Integer value){
        return Arrays.asList(values()).stream().filter(item->item.getValue().equals(value)).findFirst().orElse(null);
    }
}
