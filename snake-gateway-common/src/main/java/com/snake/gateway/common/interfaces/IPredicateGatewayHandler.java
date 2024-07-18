package com.snake.gateway.common.interfaces;

import com.snake.gateway.common.bean.GatewayContext;
import com.snake.gateway.common.enums.GatewayActionEnum;

public interface IPredicateGatewayHandler extends IGatewayHandler {

    @Override
    default GatewayActionEnum before(GatewayContext context) {
        if (predicate(context)) {
            GatewayActionEnum actionEnum = process(context);
            if (!GatewayActionEnum.CONTINUE.equals(actionEnum)) {
                return actionEnum;
            }
        }
        return GatewayActionEnum.CONTINUE;
    }


    /**
     * 断言
     *
     * @param context
     * @return
     */
    boolean predicate(GatewayContext context);

    /**
     * 过程处理
     *
     * @param context
     * @return
     */
    GatewayActionEnum process(GatewayContext context);
}
