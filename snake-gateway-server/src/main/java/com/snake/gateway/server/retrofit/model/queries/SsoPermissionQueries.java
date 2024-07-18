package com.snake.gateway.server.retrofit.model.queries;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "鉴权接口-用户权限查询条件对象")
public class SsoPermissionQueries {

    @Schema(name = "账号ID")
    private String accountId;

    @Schema(name = "账号渠道")
    private String loginType;
}
