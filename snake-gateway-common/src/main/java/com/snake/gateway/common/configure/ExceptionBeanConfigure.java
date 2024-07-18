package com.snake.gateway.common.configure;

import cn.dev33.satoken.exception.InvalidContextException;
import com.google.common.base.Throwables;
import com.snake.gateway.common.bean.ErrorCode;
import com.snake.gateway.common.exception.IExceptionProcess;
import io.github.yxsnake.pisces.web.core.base.Result;
import io.github.yxsnake.pisces.web.core.exception.BizException;
import io.netty.channel.ConnectTimeoutException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;

import java.net.UnknownHostException;

@Slf4j
@Configuration
public class ExceptionBeanConfigure {

    @Bean
    public IExceptionProcess bizExceptionProcess() {
        return new IExceptionProcess() {
            @Override
            public boolean predicate(Throwable throwable) {
                if(throwable instanceof BizException){
                    return true;
                }
                if(throwable instanceof InvalidContextException){
                    return true;
                }
                return throwable instanceof BizException;
            }

            @Override
            public Result process(ServerWebExchange exchange, Throwable throwable) {
                log.error("throwable : {}", Throwables.getStackTraceAsString(throwable));
                if(throwable instanceof BizException){
                    BizException bizException = (BizException) throwable;
                    return bizException.getResult();
                }else if(throwable instanceof InvalidContextException){
                    InvalidContextException exception = (InvalidContextException) throwable;
                    return Result.builder()
                            .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .msg(exception.getMessage())
                            .build();
                }else {
                    return Result.builder()
                            .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .msg("未知异常")
                            .build();
                }
            }
        };
    }


    @Bean
    public IExceptionProcess responseStatusException() {
        return new IExceptionProcess() {
            @Override
            public boolean predicate(Throwable throwable) {
                return throwable instanceof ResponseStatusException;
            }

            @Override
            public Result process(ServerWebExchange exchange, Throwable throwable) {
                String msg = buildMessage(exchange.getRequest(), throwable);
                return Result.builder().code(ErrorCode.SYSTEM_ERROR.getCode()).msg(msg).build();
            }
        };
    }

    @Bean
    public IExceptionProcess unknownHostException() {
        return new IExceptionProcess() {
            @Override
            public boolean predicate(Throwable throwable) {
                return throwable instanceof UnknownHostException;
            }

            @Override
            public Result process(ServerWebExchange exchange, Throwable throwable) {
                log.error("服务异常", throwable);
                return Result.builder().code(ErrorCode.SYSTEM_ERROR.getCode()).msg(throwable.getMessage()).build();
            }
        };
    }

    @Bean
    public IExceptionProcess connectTimeoutException() {
        return new IExceptionProcess() {
            @Override
            public boolean predicate(Throwable throwable) {
                return throwable instanceof ConnectTimeoutException;
            }

            @Override
            public Result process(ServerWebExchange exchange, Throwable throwable) {
                log.error("服务异常", throwable);
                return Result.builder().code(ErrorCode.SYSTEM_ERROR.getCode()).msg(throwable.getMessage()).build();
            }
        };
    }

    private String buildMessage(ServerHttpRequest request, Throwable ex) {
        StringBuilder message = new StringBuilder("gateway Failed to handle request [");
        message.append(request.getMethod());
        message.append(" ");
        message.append(request.getURI().getRawPath());
        message.append("]");
        if (ex != null) {
            message.append(": ");
            message.append(ex.getMessage());
        }
        return message.toString();
    }
}
