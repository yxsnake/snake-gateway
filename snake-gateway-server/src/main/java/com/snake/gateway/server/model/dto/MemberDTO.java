package com.snake.gateway.server.model.dto;

import com.snake.gateway.server.retrofit.model.enums.AccountChannelEnum;
import io.github.yxsnake.pisces.web.core.base.LoginUser;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.Objects;

@Data
public class MemberDTO {

    private String memberId;
    private Integer channel;
    private String accountId;
    private String nickName;
    private String avatar;
    private String phone;
    private String email;
    private String tenantId;

    public LoginUser convertLoginUser() {
        LoginUser loginUser = new LoginUser();
        if (Objects.nonNull(this)) {
            BeanUtils.copyProperties(this, loginUser);
            AccountChannelEnum accountChannelEnum = AccountChannelEnum.getInstance(this.channel);
            if(AccountChannelEnum.MEMBER.equals(accountChannelEnum)){
                loginUser.setUserId(memberId);
            }
            BeanUtils.copyProperties(this, loginUser);
        } else {
            loginUser = LoginUser.defaultUser();
        }
        return loginUser;
    }
}
