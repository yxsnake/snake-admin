package com.snake.admin.common.enums;

import io.github.yxsnake.pisces.web.core.base.IBaseEnum;
import lombok.Getter;

import java.util.Arrays;

/**
 * @author: snake
 * @create-time: 2024-08-03
 * @description: 页面动画类型
 * @version: 1.0
 */
@Getter
public enum AnimationEnum implements IBaseEnum<String> {

    BOUNCE("bounce","bounce"),

    FLASH("flash","flash"),

    PULSE("pulse","pulse"),

    RUBBER_BAND("rubberBand","rubberBand"),

    ;

    private final String value;

    private final String label;

    AnimationEnum(final String value,final String label){
        this.value = value;
        this.label = label;
    }

    public static AnimationEnum getInstance(final Integer value){
        return Arrays.asList(values()).stream().filter(item->item.getValue().equals(value)).findFirst().orElse(null);
    }
}
