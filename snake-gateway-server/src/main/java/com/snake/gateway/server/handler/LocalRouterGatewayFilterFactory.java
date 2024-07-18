package com.snake.gateway.server.handler;

import com.snake.gateway.server.util.StringPool;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.http.server.reactive.ServerHttpRequest;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LocalRouterGatewayFilterFactory extends AbstractGatewayFilterFactory<Object> {

    private final static String HTTP_STR = "http";

    @Override
    public String name() {
        return "localFilter";
    }

    @Override
    public GatewayFilter apply(Object o) {
        return (exchange, chain) -> {
            String rawPath = exchange.getRequest().getURI().getRawPath();
            String[] split1 = rawPath.split("\\/");
            String routerServerName = split1[1];
            String[] split = routerServerName.substring(2).split("-");
            String port = split[4];
            String ipAndPort = Stream.of(split[0], split[1], split[2], split[3]).collect(Collectors.joining(".")) + StringPool.COLON + port;


            Route oldRoute = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR);
            Route route = Route.async().predicate((key) -> true).id(oldRoute.getId()).uri(HTTP_STR + StringPool.COLON + StringPool.DOUBLE_SLASH + ipAndPort).build();
            exchange.getAttributes().put(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR, route);


            ServerHttpRequest req = exchange.getRequest();
            ServerWebExchangeUtils.addOriginalRequestUrl(exchange, req.getURI());
            String path = req.getURI().getRawPath();
            String newPath = path.replaceAll(routerServerName, "");
            ServerHttpRequest request = req.mutate().path(newPath).build();
            exchange.getAttributes().put(ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR, request.getURI());
            return chain.filter(exchange.mutate().request(request).build());
        };
    }

}
