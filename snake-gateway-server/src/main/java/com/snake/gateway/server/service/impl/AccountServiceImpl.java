package com.snake.gateway.server.service.impl;

import com.snake.gateway.server.retrofit.client.AccountClient;
import com.snake.gateway.server.retrofit.model.dto.AccountDTO;
import com.snake.gateway.server.retrofit.model.enums.AccountChannelEnum;
import com.snake.gateway.server.retrofit.model.queries.DefaultTenantAccountEqualsQueries;
import com.snake.gateway.server.service.AccountService;
import io.github.yxsnake.pisces.web.core.base.Result;
import io.github.yxsnake.pisces.web.core.utils.BizAssert;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountClient accountClient;

    @Override
    public AccountDTO findByAccount(String account, String password, AccountChannelEnum accountChannelEnum) {
        DefaultTenantAccountEqualsQueries accountEqualsQueries = new DefaultTenantAccountEqualsQueries();
        accountEqualsQueries.setAccount(account);
        accountEqualsQueries.setPassword(password);
        accountEqualsQueries.setChannel(accountChannelEnum.getValue());
        Result<AccountDTO> result = accountClient.findDefaultTenantAccount(accountEqualsQueries);
        BizAssert.isTrue(result.getMsg(),!Result.isSuccess(result));
        return result.getData();
    }
}
