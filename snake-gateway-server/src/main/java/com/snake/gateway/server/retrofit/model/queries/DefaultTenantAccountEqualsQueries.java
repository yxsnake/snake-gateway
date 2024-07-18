package com.snake.gateway.server.retrofit.model.queries;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

/**
 * @author: snake
 * @create-time: 2024-06-26
 * @description: 默认租户账号查询请求参数对象
 * @version: 1.0
 */
@Data
@Schema(name = "默认租户账号查询请求参数对象")
public class DefaultTenantAccountEqualsQueries implements Serializable {

    @Schema(name = "账号")
    @NotBlank(message = "账号不能为空")
    private String account;

    @Schema(name = "密码")
    @NotBlank(message = "密码不能为空")
    private String password;

    @Schema(name = "渠道")
    @NotNull(message = "渠道不能为空")
    private Integer channel;
}

