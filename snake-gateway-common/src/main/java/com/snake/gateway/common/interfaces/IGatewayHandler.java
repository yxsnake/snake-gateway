package com.snake.gateway.common.interfaces;

import com.snake.gateway.common.bean.GatewayContext;
import com.snake.gateway.common.enums.GatewayActionEnum;

public interface IGatewayHandler {
    default GatewayActionEnum before(GatewayContext context) {
        return GatewayActionEnum.CONTINUE;
    }

    default void after(GatewayContext context) {
    }

    int sort();

    default String group() {
        return null;
    }
}
