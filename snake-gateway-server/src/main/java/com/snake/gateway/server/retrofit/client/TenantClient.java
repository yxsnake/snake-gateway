package com.snake.gateway.server.retrofit.client;

import com.github.lianjiatech.retrofit.spring.boot.core.RetrofitClient;
import com.snake.gateway.server.retrofit.model.dto.TenantDTO;
import io.github.yxsnake.pisces.web.core.base.Result;
import retrofit2.http.GET;
import retrofit2.http.Query;

@RetrofitClient(baseUrl = "${base.url.snake-system-server}")
public interface TenantClient {


    /**
     * 查询租户详情
     * @param tenantId
     * @return
     */
    @GET(value = "tenant/detail")
    Result<TenantDTO> detail(@Query("tenantId") String tenantId);
}
