package com.snake.gateway.server.configure;

import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.exception.SaTokenException;
import cn.dev33.satoken.reactor.context.SaReactorSyncHolder;
import cn.dev33.satoken.reactor.filter.SaReactorFilter;
import cn.dev33.satoken.router.SaHttpMethod;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.google.common.base.Throwables;
import com.snake.gateway.server.configure.properties.UserAuthProperties;
import com.snake.gateway.server.model.enums.SaTokenResultCode;
import com.snake.gateway.server.retrofit.model.dto.ResourceDTO;
import com.snake.gateway.server.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;

import java.util.List;
import java.util.Objects;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class SaTokenConfiguration {

    private final UserAuthProperties userAuthProperties;

    private final UserService userService;




    /**
     * 注册 [Sa-Token全局过滤器]
     */
    @Bean
    public SaReactorFilter getSaReactorFilter() {
        return new SaReactorFilter()
                // 指定 [拦截路由]  拦截所有path
                .addInclude("/**")
                // 指定 [放行路由]
                .setExcludeList(userAuthProperties.getExcludePaths())
                // 指定[认证函数]: 每次请求执行
                .setAuth(obj -> {
                    log.info("---------- sa全局认证");
                    List<ResourceDTO> list = userService.getAuthList(String.valueOf(StpUtil.getLoginId()));
                    list.stream().forEach(item->{
                        log.info("[鉴权初始化]---> 路径:{},需要权限：{}",item.getPath(),item.getPerm());
                        SaRouter.match(item.getPath(),r->StpUtil.checkPermission(item.getPerm()));
                    });
                })
                // 异常处理函数：每次认证函数发生异常时执行此函数
                .setError(e1 -> {
                    e1.printStackTrace();
                    // 设置错误返回格式为JSON
                    ServerWebExchange exchange = SaReactorSyncHolder.getContext();

                    log.error("sa-token异常:{}", Throwables.getStackTraceAsString(e1));
                    // 设置响应头
                    SaHolder.getResponse().setHeader("Content-Type", "application/json;charset=UTF-8");

                    ServerHttpResponse response = exchange.getResponse();

                    response.setStatusCode(HttpStatus.OK);
                    response.getHeaders().add("Content-Type", "application/json; charset=utf-8");

                    /**
                     * sa-token登录相关异常处理
                     * https://sa-token.cc/v/v1.36.0/doc.html#/fun/exception-code
                     */
                    if (e1 instanceof SaTokenException) {
                        log.info("SaTokenConfigure error {}", Throwables.getStackTraceAsString(e1));
                        SaTokenException e = (SaTokenException) e1;
                        SaTokenResultCode saTokenResultCode = SaTokenResultCode.getInstance(String.valueOf(e.getCode()));
                        if (Objects.nonNull(saTokenResultCode)) {
                            return SaResult.error(saTokenResultCode.getMsg());
                        }
                        return SaResult.error(e1.getMessage());
                    }
                    return SaResult.error("登录异常，请联系管理员处理...");
                })
                .setBeforeAuth(obj -> {
                    // ---------- 设置跨域响应头 ----------
                    SaHolder.getResponse()
                            // 允许指定域访问跨域资源
                            .setHeader("Access-Control-Allow-Origin", "*")
                            // 允许所有请求方式
                            .setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE")
                            // 有效时间
                            .setHeader("Access-Control-Max-Age", "3600")
                            // 允许的header参数
                            .setHeader("Access-Control-Allow-Headers", "*");

                    // 如果是预检请求，则立即返回到前端
                    SaRouter.match(SaHttpMethod.OPTIONS)
                            .free(r ->
                                    log.info("--------OPTIONS预检请求，不做处理")
                            )
                            .back();
                })
                ;
    }

}
