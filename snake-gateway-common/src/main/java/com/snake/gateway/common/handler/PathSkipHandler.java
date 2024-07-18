package com.snake.gateway.common.handler;

import com.snake.gateway.common.bean.GatewayContext;
import com.snake.gateway.common.cons.GatewaySortCons;
import com.snake.gateway.common.enums.GatewayActionEnum;
import com.snake.gateway.common.interfaces.IPredicateGatewayHandler;
import com.snake.gateway.common.interfaces.match.IPathSkipMatch;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ServerWebExchange;

import java.util.List;

@Slf4j
@AllArgsConstructor
public class PathSkipHandler implements IPredicateGatewayHandler {
    private final List<IPathSkipMatch> skipMatchPathList;

    @Override
    public boolean predicate(GatewayContext context) {
        if (CollectionUtils.isEmpty(skipMatchPathList)) {
            return false;
        }
        ServerWebExchange exchange = context.getExchange();
        String path = exchange.getRequest().getURI().getPath();
        for (IPathSkipMatch skipMatchPath : skipMatchPathList) {
            if (skipMatchPath.match(path)) {
                log.info("PathSkipProcess 匹配到{}", path);
                return true;
            }
        }
        return false;
    }

    @Override
    public int sort() {
        return GatewaySortCons.HANDLER_PATH_SKIP;
    }

    @Override
    public GatewayActionEnum process(GatewayContext context) {
        return GatewayActionEnum.ROUTER;
    }


}
