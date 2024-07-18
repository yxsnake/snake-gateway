package com.snake.gateway.common.filter;

import com.snake.gateway.common.bean.ErrorCode;
import com.snake.gateway.common.bean.GatewayContext;
import com.snake.gateway.common.enums.GatewayActionEnum;
import com.snake.gateway.common.exception.IExceptionProcess;
import com.snake.gateway.common.interfaces.IGatewayHandler;
import com.snake.gateway.common.util.ResponseUtil;
import com.snake.gateway.common.util.ServerWebExchangeUtil;
import io.github.yxsnake.pisces.web.core.base.Result;
import io.github.yxsnake.pisces.web.core.utils.BizAssert;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE + 1)
public class IndexFilter implements WebFilter {
    private final List<IGatewayHandler> gatewayHandlerList;
    private final List<IExceptionProcess> exceptionProcessList;

    public IndexFilter(
            List<IGatewayHandler> gatewayHandlerList,
            List<IExceptionProcess> exceptionProcessList
    ) {
        this.gatewayHandlerList = gatewayHandlerList;
        this.exceptionProcessList = exceptionProcessList;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        GatewayContext context = new GatewayContext(exchange);
        int i = 0;
        Set<Integer> skipSet = null;
        String group = null;
        for (; i < gatewayHandlerList.size(); i++) {
            IGatewayHandler exchangeHandler = gatewayHandlerList.get(i);
            String handlerGroup = exchangeHandler.group();
            /**
             *  handler没有group时为全局handler,程序通过处理
             *  handler有group时会根据第一次使用group做处理，不同group跳过
             */
            if (null != handlerGroup) {
                if (null == group) {
                    group = handlerGroup;
                } else {
                    if (!group.equals(handlerGroup)) {
                        continue;
                    }
                }
            }
            try {
                GatewayActionEnum action = exchangeHandler.before(context);
                if (GatewayActionEnum.CONTINUE.equals(action)) {
                    //继续
                    continue;
                } else if (GatewayActionEnum.ROUTER.equals(action)) {
                    //直接路由
                    break;
                } else if (GatewayActionEnum.RESULT.equals(action)) {
                    //结果返回
                    after(i, context, group);
                    Result result = context.getResult();
                    exchange = context.getExchange();
                    return ResponseUtil.resultResponse(exchange.getResponse(), result);
                } else if (GatewayActionEnum.REDIRECT.equals(action)) {
                    //重定向
                    after(i, context, group);
                    String redirectUrl = context.getRedirectUrl();
                    exchange = context.getExchange();
                    return ResponseUtil.redirect(exchange, redirectUrl);
                } else {
                    BizAssert.isTrue("未处理结果",true);
                }
            } catch (Throwable e) {
                return onException(i, context, e);
            }
        }

        ServerWebExchange finalExchange = ServerWebExchangeUtil.buildServerWebExchange(context);
        int finalI = i - 1;
        return chain.filter(finalExchange).then(afterExecute(finalI, context, group)).onErrorResume(e -> onException(
                finalI, context, e));
    }

    /**
     * 返回结果时执行
     *
     * @param context
     * @return
     */
    private Mono<Void> afterExecute(int idx, GatewayContext context, String group) {
        return Mono.fromRunnable(() -> {
            after(idx, context, group);
        });
    }

    /**
     * 处理handleer after方法
     *
     * @param context
     */
    private void after(int idx, GatewayContext context, String group) {
        for (int i = idx; i >= 0; i--) {
            IGatewayHandler exchangeHandler = gatewayHandlerList.get(i);
            if (null != group && null != exchangeHandler.group()) {
                if (!group.equals(exchangeHandler.group())) {
                    continue;
                }
            }
            exchangeHandler.after(context);
        }
    }


    /**
     * 错误时执行
     *
     * @param context
     * @return
     */
    private Mono<Void> onException(int idx, GatewayContext context, Throwable e) {
        for (int i = idx; i >= 0; i--) {
            IGatewayHandler exchangeHandler = gatewayHandlerList.get(i);
            exchangeHandler.after(context);
        }
        for (IExceptionProcess exceptionProcess : exceptionProcessList) {
            if (exceptionProcess.predicate(e)) {
                Result result = exceptionProcess.process(context.getExchange(), e);
                return ResponseUtil.resultResponse(context.getExchange().getResponse(), result);
            }
        }
        log.error("拦截到异常", e);
        return ResponseUtil.resultResponse(context.getExchange().getResponse(), Result.builder().code(
                ErrorCode.SYSTEM_ERROR.getCode()).msg(ErrorCode.SYSTEM_ERROR.getMsg()).build());
    }

    private Set<Integer> addSkipSort(Set<Integer> set, int sort) {
        if (null == set) {
            set = new HashSet<>();
        }
        set.add(sort);
        return set;
    }
}
