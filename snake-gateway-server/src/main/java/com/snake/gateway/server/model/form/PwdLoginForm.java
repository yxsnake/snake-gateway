package com.snake.gateway.server.model.form;

import cn.hutool.core.util.StrUtil;
import io.github.yxsnake.pisces.web.core.utils.BizAssert;
import lombok.Data;

import java.util.Objects;

@Data
public class PwdLoginForm {

    /**
     * 账号
     */
    private String account;
    /**
     * 密码
     */
    private String password;
    /**
     * 记住我
     */
    private Integer isRememberMe;
    /**
     * PC WX APP
     */
    private String loginWay = "PC";

    private Integer channel;


    public void notNullCheck() {
        BizAssert.isTrue("账号不能为空", StrUtil.isBlank(account));
        BizAssert.isTrue("密码不能为空", StrUtil.isBlank(password));
        BizAssert.isTrue("渠道类型不能为空", Objects.isNull(channel));
    }

}
