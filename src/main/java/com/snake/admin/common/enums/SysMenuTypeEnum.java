package com.snake.admin.common.enums;

import io.github.yxsnake.pisces.web.core.base.IBaseEnum;
import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;

/**
 * @author: snake
 * @create-time: 2024-08-03
 * @description:
 * @version: 1.0
 */
@Getter
public enum SysMenuTypeEnum implements IBaseEnum<Integer> {
    DIR(0,"目录"),

    MENU(1,"菜单"),

    IFRAME(2,"iframe"),

    LINK(3,"外链"),

    BUTTON(4,"按钮")
    ;

    private final Integer value;

    private final String label;

    SysMenuTypeEnum(final int value,final String label){
        this.value = value;
        this.label = label;
    }

    public static SysMenuTypeEnum getInstance(final Integer value){
        return Arrays.asList(values()).stream().filter(item->item.getValue().equals(value)).findFirst().orElse(null);
    }
}
