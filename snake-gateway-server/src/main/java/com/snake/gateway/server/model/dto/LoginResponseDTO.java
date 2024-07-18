package com.snake.gateway.server.model.dto;

import io.github.yxsnake.pisces.web.core.base.LoginUser;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponseDTO {

    /**
     * 令牌
     */
    private String token;
    /**
     * 令牌过期时间
     */
    private Long exp;
    /**
     * 用户信息
     */
    private LoginUser loginUser;
    /**
     * 传输是否加密
     */
    private Boolean encrypt;


}
