package com.snake.gateway.server.retrofit.client;

import com.github.lianjiatech.retrofit.spring.boot.core.RetrofitClient;
import com.snake.gateway.server.retrofit.model.dto.EmpDTO;
import io.github.yxsnake.pisces.web.core.base.Result;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * @author: snake
 * @create-time: 2024-07-04
 * @description: 员工相关 API 声明
 * @version: 1.0
 */
@RetrofitClient(baseUrl = "${base.url.snake-system-server}")
public interface EmpClient {

    @GET(value = "emp/account")
    Result<EmpDTO> get(@Query("accountId") String accountId);
}
