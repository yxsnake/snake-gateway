package com.snake.gateway.common.cons;

public class GatewaySortCons {

    /**
     * 记录时间
     */
    public static final int HANDLER_TIME = Integer.MIN_VALUE;

    /**
     * 健康检查
     */
    public static final int HEALTH_CHECK = Integer.MIN_VALUE + 1;

    /**
     * 跨域信息删除
     */
    public static final int HANDLER_CORS_REMOVE = Integer.MIN_VALUE + 10;

    /**
     * 跨域信息添加
     */
    public static final int HANDLER_CORS_ADD = HANDLER_CORS_REMOVE + 1;

    /**
     * ip黑名单
     * 直接拒绝
     */
    public static final int HANDLER_IP_BLACK = HANDLER_CORS_ADD + 100;

    /**
     * ip白名单
     * 通过后继续
     */
    public static final int HANDLER_IP_WHITE = HANDLER_IP_BLACK + 1;

    /**
     * 请求路径拒绝
     * 直接拒绝
     */
    public static final int HANDLER_PATH_DELAY = HANDLER_IP_BLACK + 100;

    /**
     * 请求次数限制
     * 超过次数拒绝
     */
    public static final int HANDLER_REQUEST_LIMITER = HANDLER_PATH_DELAY + 100;

    /**
     * ip次数限制
     * 超过次数拒绝
     */
    public static final int HANDLER_IP_LIMITER = HANDLER_REQUEST_LIMITER + 100;


    /**
     * 路径body解密
     */
    public static final int PATH_BODY_DECRYPT = HANDLER_IP_LIMITER + 100;

    /**
     * 路径跳过
     * 跳过登录直接路由
     * -100
     */
    public static final int HANDLER_PATH_SKIP_LOGIN = -1000;

    /**
     * 设置token值
     */
    public static final int HANDLER_TOKEN_SET_SORT = HANDLER_PATH_SKIP_LOGIN + 100;

    /**
     * 时间戳token校验
     * 前后端约定加密校验
     * -21474835086
     */
    public static final int HANDLER_TIMESTAMP_TOKEN_CHECK = HANDLER_TOKEN_SET_SORT + 100;

    public static final int USER_CONTEXT = HANDLER_TIMESTAMP_TOKEN_CHECK + 100;

    /**
     * 路径跳过
     * 跳过登录直接路由
     */
    public static final int HANDLER_PATH_SKIP = -100;

    // --------------- 登录之前<0  0<=登录<=100  登录后>100 -----------

}
