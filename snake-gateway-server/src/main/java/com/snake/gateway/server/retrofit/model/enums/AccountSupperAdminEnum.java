package com.snake.gateway.server.retrofit.model.enums;

import io.github.yxsnake.pisces.web.core.base.IBaseEnum;
import lombok.Getter;

import java.util.Arrays;

/**
 * @author: snake
 * @create-time: 2024-06-25
 * @description: 账号是否超级管理员 枚举
 * @version: 1.0
 */
@Getter
public enum AccountSupperAdminEnum implements IBaseEnum<Integer> {
    SUPPER(1,"租户超级管理员"),

    ORDINARY(0,"普通用户"),

    ;

    private final Integer value;

    private final String label;

    AccountSupperAdminEnum(final Integer value,final String label){
        this.value = value;
        this.label = label;
    }

    public static AccountSupperAdminEnum getInstance(final Integer value){
        return Arrays.asList(values()).stream().filter(item->item.getValue().equals(value)).findFirst().orElse(null);
    }
}

