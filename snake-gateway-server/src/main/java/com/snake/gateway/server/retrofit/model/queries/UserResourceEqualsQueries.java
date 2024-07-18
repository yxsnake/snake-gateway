package com.snake.gateway.server.retrofit.model.queries;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @author: snake
 * @create-time: 2024-07-04
 * @description: 用户资源查询条件
 * @version: 1.0
 */
@Data
@Schema(name = "用户资源查询条件")
public class UserResourceEqualsQueries {

    @NotBlank(message = "用户 ID不能为空")
    private String accountId;

    private Integer resourceType;

    private String level;

}
