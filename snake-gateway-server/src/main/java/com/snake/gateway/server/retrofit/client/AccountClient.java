package com.snake.gateway.server.retrofit.client;

import com.github.lianjiatech.retrofit.spring.boot.core.RetrofitClient;
import com.snake.gateway.server.retrofit.model.dto.AccountDTO;
import com.snake.gateway.server.retrofit.model.queries.DefaultTenantAccountEqualsQueries;
import io.github.yxsnake.pisces.web.core.base.Result;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * @author: snake
 * @create-time: 2024-06-26
 * @description: 账号相关API 声明
 * @version: 1.0
 */
@RetrofitClient(baseUrl = "${base.url.snake-system-server}")
public interface AccountClient {

    /**
     * 查询默认租户账号，用户登录
     * @param queries
     * @return
     */
    @POST(value = "account/findDefaultTenantAccount")
    Result<AccountDTO> findDefaultTenantAccount(@Body DefaultTenantAccountEqualsQueries queries);

    @GET(value = "account/findByAccountId")
    Result<AccountDTO> findByAccountId(@Query("accountId") String accountId);
}

