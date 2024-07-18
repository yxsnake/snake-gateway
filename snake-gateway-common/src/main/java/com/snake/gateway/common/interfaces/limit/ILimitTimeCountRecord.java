package com.snake.gateway.common.interfaces.limit;

/**
 * 控制数量记录
 * 数量大约等于0时可访问
 */
public interface ILimitTimeCountRecord {

    /**
     * 累计
     *
     * @return 是否被限制
     */
    boolean cumulation(String ip);

    boolean isFrozen(String ip);

    void unFrozen(String ip);
}