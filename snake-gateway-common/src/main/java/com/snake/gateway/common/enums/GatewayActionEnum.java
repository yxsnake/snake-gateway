package com.snake.gateway.common.enums;

/**
 * mono返回执行动作枚举
 */
public enum GatewayActionEnum {
    /**
     * 继续
     */
    CONTINUE,
    /**
     * 结果返回
     */
    RESULT,
    /**
     * 直接路由
     */
    ROUTER,
    /**
     * 重定向
     */
    REDIRECT,
    ;

}