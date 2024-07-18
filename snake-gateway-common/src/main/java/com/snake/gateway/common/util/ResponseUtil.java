package com.snake.gateway.common.util;

import com.alibaba.fastjson2.JSON;
import io.github.yxsnake.pisces.web.core.base.Result;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

public class ResponseUtil {

    public static Mono<Void> resultResponse(ServerHttpResponse response, Result result) {
        return resultResponse(response, JSON.toJSONString(result));
    }

    public static Mono<Void> resultResponse(ServerHttpResponse response, String context) {
        HttpHeaders httpHeaders = response.getHeaders();
        httpHeaders.add("Content-Type", "application/json; charset=UTF-8");
        response.setStatusCode(HttpStatus.OK);
        DataBuffer dataBuffer = response.bufferFactory().wrap(context.getBytes(StandardCharsets.UTF_8));
        Mono<Void> voidMono = response.writeWith(Mono.just(dataBuffer));
        return voidMono;
    }

    public static void headersAddOrigin(ServerHttpRequest request, ServerHttpResponse response) {
        /*HttpHeaders headersReq = request.getHeaders();
        HttpHeaders headersResp = response.getHeaders();
        String origin = headersReq.getOrigin();
        if(StringUtil.isNotEmpty(origin)){
            headersResp.add(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, headersReq.getOrigin());
        }
        headersResp.add(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, "POST, GET, PUT, OPTIONS, DELETE, PATCH");
        headersResp.add(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true");
        List<String> accessControlRequestHeaders = headersReq.getAccessControlRequestHeaders();
        if(CollectionUtils.isEmpty(accessControlRequestHeaders)){
            headersResp.addAll(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, headersReq.getAccessControlRequestHeaders());
        }
        headersResp.add(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, StringDef.ASTERISK);
        headersResp.add(HttpHeaders.ACCESS_CONTROL_MAX_AGE, MAX_AGE);*/

    }


    public static Mono<Void> redirect(ServerWebExchange exchange, String url) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.SEE_OTHER);
        response.getHeaders().set("Location", url);
        return exchange.getResponse().setComplete();
    }
}
