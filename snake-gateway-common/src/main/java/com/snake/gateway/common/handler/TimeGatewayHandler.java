package com.snake.gateway.common.handler;

import com.snake.gateway.common.bean.GatewayContext;
import com.snake.gateway.common.cons.GatewaySortCons;
import com.snake.gateway.common.enums.GatewayActionEnum;
import com.snake.gateway.common.interfaces.IGatewayHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TimeGatewayHandler implements IGatewayHandler {
    private final String START_TIME = "startTime";
    private final String LOG_INFO = "webInfoLog -> path:[{}] .执行时间:[{}ms]";

    @Override
    public GatewayActionEnum before(GatewayContext context) {
        context.getExchange().getAttributes().put(START_TIME, System.currentTimeMillis());
        return GatewayActionEnum.CONTINUE;
    }

    @Override
    public void after(GatewayContext context) {
        Long start = context.getExchange().getAttribute(START_TIME);
        long executeTime = System.currentTimeMillis() - start;
        log.info(LOG_INFO, context.getExchange().getRequest().getURI().getRawPath(), executeTime);
    }

    @Override
    public int sort() {
        return GatewaySortCons.HANDLER_TIME;
    }
}
