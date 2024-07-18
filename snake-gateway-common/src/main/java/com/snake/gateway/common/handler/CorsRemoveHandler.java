package com.snake.gateway.common.handler;

import com.snake.gateway.common.bean.GatewayContext;
import com.snake.gateway.common.cons.GatewaySortCons;
import com.snake.gateway.common.enums.GatewayActionEnum;
import com.snake.gateway.common.interfaces.IGatewayHandler;
import org.reactivestreams.Publisher;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import reactor.core.publisher.Mono;

public class CorsRemoveHandler implements IGatewayHandler {


    @Override
    public GatewayActionEnum before(GatewayContext context) {
        ServerHttpResponse response = new ServerHttpResponseDecorator(context.getExchange().getResponse()) {

            @Override
            public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                this.getHeaders().remove(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN);
                this.getHeaders().remove(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS);
                this.getHeaders().remove(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS);
                this.getHeaders().remove(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS);
                return super.writeWith(body);
            }
        };
        context.setExchange(context.getExchange().mutate().response(response).build());
        return GatewayActionEnum.CONTINUE;
    }

    @Override
    public int sort() {
        return GatewaySortCons.HANDLER_CORS_REMOVE;
    }
}
