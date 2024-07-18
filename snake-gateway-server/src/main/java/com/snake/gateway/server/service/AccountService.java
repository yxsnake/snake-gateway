package com.snake.gateway.server.service;


import com.snake.gateway.server.retrofit.model.dto.AccountDTO;
import com.snake.gateway.server.retrofit.model.enums.AccountChannelEnum;

public interface AccountService {


    AccountDTO findByAccount(String account, String password, AccountChannelEnum accountChannelEnum);
}
