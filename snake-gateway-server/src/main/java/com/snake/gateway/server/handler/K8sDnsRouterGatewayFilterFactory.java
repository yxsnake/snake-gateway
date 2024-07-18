package com.snake.gateway.server.handler;

import com.snake.gateway.server.bean.CustomServer;
import com.snake.gateway.server.bean.K8sServer;
import com.snake.gateway.server.util.StringPool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * k8专用过滤器
 */
@Slf4j
public class K8sDnsRouterGatewayFilterFactory extends AbstractGatewayFilterFactory<Object> {

    private final static String HTTP_STR = "http";
    private final static String DEFAULT_PORT = "21001";
    private final static String SERVICE_NAME_STR = "serverName";
    private final static String API_SRT = "-api";
    private final Map<String, K8sServer> k8sServer;
    private final Map<String, CustomServer> customServerMap;

    public K8sDnsRouterGatewayFilterFactory(Map<String, K8sServer> k8sServerMap, Map<String, CustomServer> customServerMap) {
        this.k8sServer = k8sServerMap;
        this.customServerMap = customServerMap;
    }

    @Override
    public String name() {
        return "k8sDnsFilter";
    }

    @Override
    public GatewayFilter apply(Object o) {
        return (exchange, chain) -> {
            Route oldRoute = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR);
            Map<String, String> map = exchange.getAttribute(ServerWebExchangeUtils.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
            String serverName = map.get(SERVICE_NAME_STR);
            String applicationName = serverName + API_SRT;
            K8sServer k8sServer = this.k8sServer.get(applicationName);
            String pathPrefix = null;
            String routerUri = null;

            if (null == k8sServer) {
                CustomServer customServer = customServerMap.get(applicationName);
                if (null != customServer) {
                    Integer port = customServer.getPort();
                    if (StringUtils.isEmpty(port)) {
                        routerUri = HTTP_STR + StringPool.COLON + StringPool.DOUBLE_SLASH + customServer.getHost();
                        Route route = Route.async().predicate((key) -> true).id(oldRoute.getId()).uri(routerUri).build();
                        exchange.getAttributes().put(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR, route);
                    } else {
                        routerUri = HTTP_STR + StringPool.COLON + StringPool.DOUBLE_SLASH + customServer.getHost() + StringPool.COLON + customServer.getPort();
                        Route route = Route.async().predicate((key) -> true).id(oldRoute.getId()).uri(routerUri).build();
                        exchange.getAttributes().put(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR, route);
                    }
                    pathPrefix = customServer.getPath();
                } else {
                    routerUri = HTTP_STR + StringPool.COLON + StringPool.DOUBLE_SLASH + applicationName + StringPool.COLON + DEFAULT_PORT;
                    Route route = Route.async().predicate((key) -> true).id(oldRoute.getId()).uri(routerUri).build();
                    exchange.getAttributes().put(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR, route);
                }
            } else {
                if (!StringUtils.isEmpty(k8sServer.getPort())) {
                    routerUri = HTTP_STR + StringPool.COLON + StringPool.DOUBLE_SLASH + k8sServer.getHost() + StringPool.COLON + k8sServer.getPort();
                } else {
                    routerUri = HTTP_STR + StringPool.COLON + StringPool.DOUBLE_SLASH + k8sServer.getHost();
                }
                Route route = Route.async().predicate((key) -> true).id(oldRoute.getId()).uri(routerUri).build();
                exchange.getAttributes().put(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR, route);
            }

            ServerHttpRequest req = exchange.getRequest();
            ServerWebExchangeUtils.addOriginalRequestUrl(exchange, req.getURI());
            String path = req.getURI().getRawPath();
            if (null != pathPrefix && pathPrefix.length() > 1) {
                path = pathPrefix + path;
            }
            String newPath = path.replaceAll(StringPool.SLASH + applicationName, "");
            log.debug("K8sDnsRouterGatewayFilterFactory new router uri:{} , path:{}, header:{}", routerUri, newPath, req.getHeaders().toString());
            ServerHttpRequest request = req.mutate().path(newPath).build();
            exchange.getAttributes().put(ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR, request.getURI());
            return chain.filter(exchange.mutate().request(request).build());
        };
    }

}
