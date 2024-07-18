package com.snake.gateway.common.handler;

import com.snake.gateway.common.bean.GatewayContext;
import com.snake.gateway.common.cons.GatewaySortCons;
import com.snake.gateway.common.enums.GatewayActionEnum;
import com.snake.gateway.common.interfaces.IPredicateGatewayHandler;
import io.github.yxsnake.pisces.web.core.base.Result;
import org.springframework.beans.factory.annotation.Value;

public class HealthCheckHandler implements IPredicateGatewayHandler {

    @Value("${gateway.handler.healthCheck.path:/health}")
    private String path;

    @Override
    public int sort() {
        return GatewaySortCons.HEALTH_CHECK;
    }

    @Override
    public boolean predicate(GatewayContext context) {
        return path.equals(context.getPath());
    }

    @Override
    public GatewayActionEnum process(GatewayContext context) {
        return context.result(Result.success(null));
    }
}
