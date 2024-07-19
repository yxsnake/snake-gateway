package com.snake.gateway.server.service.impl;

import cn.dev33.satoken.stp.*;
import com.snake.gateway.server.model.dto.LoginResponseDTO;
import com.snake.gateway.server.model.enums.EncryptFlagEnum;
import com.snake.gateway.server.model.form.PwdLoginForm;
import com.snake.gateway.server.retrofit.model.dto.AccountDTO;
import com.snake.gateway.server.retrofit.model.enums.AccountChannelEnum;
import com.snake.gateway.server.retrofit.model.enums.AccountStatusEnum;
import com.snake.gateway.server.retrofit.model.enums.AccountSupperAdminEnum;
import com.snake.gateway.server.service.AccountService;
import com.snake.gateway.server.service.LoginService;
import com.snake.gateway.server.service.UserService;
import io.github.yxsnake.pisces.web.core.base.LoginUser;
import io.github.yxsnake.pisces.web.core.utils.BizAssert;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {

    private final AccountService accountService;

    private final UserService userService;

    @Value("${api.transmission-encryption:false}")
    private Boolean transmissionEncryption;

    /**
     * 密码登录
     * @param form
     * @return
     */
    @Override
    public LoginResponseDTO login(PwdLoginForm form) {
        // 1.参数非空校验
        form.notNullCheck();
        AccountChannelEnum accountChannelEnum = AccountChannelEnum.getInstance(form.getChannel());
        BizAssert.isNull("不支持该渠道登录",accountChannelEnum);
        // 2.根据账号密码查询账号信息
        String account = form.getAccount();
        String password = form.getPassword();
        AccountDTO accountDTO = accountService.findByAccount(account,password,accountChannelEnum);
        // 3.校验账号
        BizAssert.isTrue("账号密码错误", Objects.isNull(accountDTO));
        // 4.校验账号是否禁用
        boolean accountDisabled = AccountStatusEnum.DISABLE.equals(AccountStatusEnum.getInstance(accountDTO.getDisabled()));
        BizAssert.isTrue("账号已禁用，请联系管理员", accountDisabled);
        String accountId = accountDTO.getAccountId();
        Integer channel = accountDTO.getChannel();
        // 设置登录渠道 ，后续 通过 StpUtil.getLoginType 可获取
        StpUtil.stpLogic.setLoginType(String.valueOf(channel));
        //这里的id是admin的id主键
        StpUtil.login(accountId,form.getLoginWay());
        // 查询用户信息
        LoginUser loginUser = null;
        if(AccountSupperAdminEnum.SUPPER.getValue().equals(accountDTO.getSupperAdmin())){
            loginUser = new LoginUser();
            loginUser.setSupperAdmin(Boolean.TRUE);
            loginUser.setAccountId(accountId);
            loginUser.setRealName("租户超级管理员");
            loginUser.setAvatar("https://i1.hdslb.com/bfs/face/98a570a6c6d6a263bcb0cba9e15e492125e9d310.jpg@120w_120h_1c");
            loginUser.setChannel(AccountChannelEnum.EMP.getValue());
            loginUser.setUserId(accountId);

        }else {
            userService.getLoginUser(accountId,String.valueOf(channel));
        }
        SaTokenInfo tokenInfo = StpUtil.getTokenInfo();
        // 传输是否加密
        EncryptFlagEnum encryptFlagEnum = EncryptFlagEnum.getInstance(transmissionEncryption);
        LoginResponseDTO loginResponseDTO = LoginResponseDTO.builder()
                .token(tokenInfo.getTokenValue())
                .exp(tokenInfo.getTokenTimeout())
                .loginUser(loginUser)
                .encrypt(Objects.nonNull(encryptFlagEnum)?encryptFlagEnum.getValue():EncryptFlagEnum.NO.getValue())
                .build();
        return loginResponseDTO;
    }
}
