package com.snake.admin.common.enums;

import io.github.yxsnake.pisces.web.core.base.IResultCode;
import lombok.Getter;

/**
 * @author: snake
 * @create-time: 2024-07-24
 * @description:
 * @version: 1.0
 */

@Getter
public enum BusinessResultCode implements IResultCode {

    INVALID_REFRESH_TOKEN(400,"无效的刷新token"),


    ;

    private final Integer code;

    private final String msg;

    BusinessResultCode(final int code,final String msg){
        this.code = code;
        this.msg = msg;
    }
}
