package com.snake.gateway.server.filter;

import cn.dev33.satoken.stp.StpUtil;
import com.snake.gateway.server.service.UserService;
import io.github.yxsnake.pisces.web.core.base.LoginUser;
import io.github.yxsnake.pisces.web.core.constant.Common;
import io.github.yxsnake.pisces.web.core.utils.JsonUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class AuthorizeFilter implements GlobalFilter {

    private final UserService userService;
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        Object loginIdDefaultNull = StpUtil.getLoginIdDefaultNull();
        String accountId = Objects.nonNull(loginIdDefaultNull)?String.valueOf(loginIdDefaultNull):null;
        String loginType = StpUtil.getLoginType();
        LoginUser loginUser = userService.getLoginUser(accountId,loginType);
        // 查询用户信息 并传递到token
        String userContext = null;
        String tokenValue = StpUtil.getTokenValue();
        try {
            userContext = URLEncoder.encode(JsonUtils.objectCovertToJson(loginUser), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        ServerHttpRequest newRequest = exchange
                .getRequest()
                .mutate()
                .header(Common.USER_CONTEXT,userContext)
                .header(Common.HEADER_AUTHORIZATION,Common.HEADER_AUTH_PREFIX+tokenValue)
                .build();
        ServerWebExchange finalRequest = exchange.mutate().request(newRequest).build();
        return chain.filter(finalRequest);
    }
}
