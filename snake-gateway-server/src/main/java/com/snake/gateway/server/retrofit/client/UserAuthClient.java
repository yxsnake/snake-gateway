package com.snake.gateway.server.retrofit.client;

import com.github.lianjiatech.retrofit.spring.boot.core.RetrofitClient;
import com.snake.gateway.server.retrofit.model.dto.ResourceDTO;
import com.snake.gateway.server.retrofit.model.queries.SsoPermissionQueries;
import com.snake.gateway.server.retrofit.model.queries.SsoRoleQueries;
import com.snake.gateway.server.retrofit.model.queries.UserResourceEqualsQueries;
import io.github.yxsnake.pisces.web.core.base.Result;
import retrofit2.http.Body;
import retrofit2.http.POST;

import java.util.List;

/**
 * @author: snake
 * @create-time: 2024-06-26
 * @description: 用户权限查询 API
 * @version: 1.0
 */
@RetrofitClient(baseUrl = "${base.url.snake-system-server}")
public interface UserAuthClient {

    @POST("user-auth/permissions")
    Result<List<String>> getPermissions(@Body SsoPermissionQueries queries);

    @POST("user-auth/roles")
    Result<List<String>> getRoles(@Body SsoRoleQueries queries);

    @POST("user-auth/getResources")
    Result<List<ResourceDTO>> getResources(@Body UserResourceEqualsQueries queries);
}
