package com.snake.gateway.server.model.enums;

import io.github.yxsnake.pisces.web.core.base.IBaseEnum;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum EncryptFlagEnum implements IBaseEnum<Boolean> {

    YES(true,"传输加密"),

    NO(false,"传输不加密"),


    ;

    private final Boolean value;

    private final String label;

    EncryptFlagEnum(final boolean value,final String label){
        this.value = value;
        this.label = label;
    }

    public static EncryptFlagEnum getInstance(final Boolean value){
        return Arrays.asList(values()).stream().filter(item->item.getValue().equals(value)).findFirst().orElse(null);
    }


}
