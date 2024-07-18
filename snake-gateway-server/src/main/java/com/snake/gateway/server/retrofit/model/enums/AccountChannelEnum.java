package com.snake.gateway.server.retrofit.model.enums;

import io.github.yxsnake.pisces.web.core.base.IBaseEnum;
import lombok.Getter;

import java.util.Arrays;

/**
 * @author: snake
 * @create-time: 2024-06-25
 * @description: 账号渠道 枚举
 * @version: 1.0
 */
@Getter
public enum AccountChannelEnum implements IBaseEnum<Integer> {

    EMP(11,"员工"),

    MEMBER(12,"会员"),

    SUPPLIER(13,"供应商"),





    ;

    private final Integer value;

    private final String label;

    AccountChannelEnum(final Integer value,final String label){
        this.value = value;
        this.label = label;
    }

    public static AccountChannelEnum getInstance(final Integer value){
        return Arrays.asList(values()).stream().filter(item->item.getValue().equals(value)).findFirst().orElse(null);
    }


}

