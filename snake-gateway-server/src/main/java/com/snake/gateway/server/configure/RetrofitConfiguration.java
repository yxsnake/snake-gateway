package com.snake.gateway.server.configure;

import com.github.lianjiatech.retrofit.spring.boot.core.RetrofitScan;

/**
 * @author: snake
 * @create-time: 2024-07-18
 * @description:
 * @version: 1.0
 */
@RetrofitScan(basePackages = {"com.snake.gateway.server.retrofit.client"})
public class RetrofitConfiguration {

}
