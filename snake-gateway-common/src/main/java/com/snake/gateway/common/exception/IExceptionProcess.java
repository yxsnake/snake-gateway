package com.snake.gateway.common.exception;

import io.github.yxsnake.pisces.web.core.base.Result;
import org.springframework.web.server.ServerWebExchange;

public interface IExceptionProcess {
    boolean predicate(Throwable throwable);

    Result process(ServerWebExchange exchange, Throwable throwable);
}