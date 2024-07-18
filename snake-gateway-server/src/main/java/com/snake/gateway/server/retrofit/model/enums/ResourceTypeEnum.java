package com.snake.gateway.server.retrofit.model.enums;

import io.github.yxsnake.pisces.web.core.base.IBaseEnum;
import lombok.Getter;

import java.util.Arrays;

/**
 * @author: snake
 * @create-time: 2024-06-25
 * @description: 资源类型 枚举
 * @version: 1.0
 */

@Getter
public enum ResourceTypeEnum implements IBaseEnum<Integer> {

    DIRECTORY(0,"目录"),

    MENU(1,"菜单"),

    BUTTON(2,"按钮"),

    LINK(3,"链接"),

    INTERFACE(4,"接口API"),


    ;

    private final Integer value;

    private final String label;

    ResourceTypeEnum(final Integer value,final String label){
        this.value = value;
        this.label = label;
    }

    public static ResourceTypeEnum getInstance(final Integer value){
        return Arrays.asList(values()).stream().filter(item->item.getValue().equals(value)).findFirst().orElse(null);
    }

}
