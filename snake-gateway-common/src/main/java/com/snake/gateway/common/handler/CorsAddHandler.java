package com.snake.gateway.common.handler;

import com.snake.gateway.common.bean.GatewayContext;
import com.snake.gateway.common.cons.GatewaySortCons;
import com.snake.gateway.common.enums.GatewayActionEnum;
import com.snake.gateway.common.interfaces.IGatewayHandler;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;

public class CorsAddHandler implements IGatewayHandler {
    @Value("${gateway.cors.add.origin:}")
    private String origin;
    @Value("${gateway.cors.add.headers:}")
    private String headers;
    @Value("${gateway.cors.add.methods:}")
    private String methods;

    @Override
    public GatewayActionEnum before(GatewayContext context) {
        HttpHeaders reqHeaders = context.getExchange().getRequest().getHeaders();
        ServerHttpResponse response = new ServerHttpResponseDecorator(context.getExchange().getResponse()) {

            @Override
            public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                this.getHeaders().add(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true");
                if (StringUtils.isEmpty(origin)) {
                    this.getHeaders().add(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, reqHeaders.getOrigin());
                } else {
                    this.getHeaders().add(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, origin);
                }
                if (StringUtils.isEmpty(headers)) {
                    this.getHeaders().addAll(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, reqHeaders.getAccessControlRequestHeaders());
                } else {
                    this.getHeaders().add(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, headers);
                }
                if (StringUtils.isEmpty(methods)) {
                    this.getHeaders().add(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, "POST, GET, PUT, OPTIONS, DELETE, PATCH");
                } else {
                    this.getHeaders().add(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, methods);
                }
                return super.writeWith(body);
            }
        };
        context.setExchange(context.getExchange().mutate().response(response).build());
        return GatewayActionEnum.CONTINUE;
    }

    @Override
    public int sort() {
        return GatewaySortCons.HANDLER_CORS_ADD;
    }
}
