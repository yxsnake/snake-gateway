package com.snake.gateway.server.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.snake.gateway.server.model.dto.LoginResponseDTO;
import com.snake.gateway.server.model.form.PwdLoginForm;
import com.snake.gateway.server.service.LoginService;
import io.github.yxsnake.pisces.web.core.base.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;


    /**
     * 用户名密码登录
     *
     * @param form
     * @return
     */
    @PostMapping(value = "/login")
    public Mono<Result<LoginResponseDTO>> login(@RequestBody PwdLoginForm form) {
        return Mono.just(Result.success(loginService.login(form)));
    }

    /**
     * 注销 请求头带上login的token的值 Bearer xxx
     *
     * @return
     */
    @PostMapping("/logout")
    public Mono<Result<Boolean>> logout() {
        StpUtil.logout();
        return Mono.just(Result.success(Boolean.TRUE));
    }
}
