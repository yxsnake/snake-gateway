package com.snake.gateway.server.service;


import com.snake.gateway.server.model.dto.LoginResponseDTO;
import com.snake.gateway.server.model.form.PwdLoginForm;

public interface LoginService {

    LoginResponseDTO login(PwdLoginForm form);
}
