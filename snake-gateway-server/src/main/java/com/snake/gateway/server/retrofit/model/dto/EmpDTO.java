package com.snake.gateway.server.retrofit.model.dto;

import com.snake.gateway.server.retrofit.model.enums.AccountChannelEnum;
import io.github.yxsnake.pisces.web.core.base.LoginUser;
import io.github.yxsnake.pisces.web.core.converter.Convert;
import io.github.yxsnake.pisces.web.core.utils.BizAssert;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author: snake
 * @create-time: 2024-06-26
 * @description: 员工传输对象
 * @version: 1.0
 */
@Schema(name = "员工传输对象")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmpDTO implements Convert, Serializable {

    @Schema(name = "员工ID")
    private String empId;

    @Schema(name = "员工姓名")
    private String realName;

    @Schema(name = "员工头像")
    private String avatar;

    @Schema(name = "邮箱")
    private String email;

    @Schema(name = "联系电话")
    private String phone;

    @Schema(name = "性别(0-女，1-男)")
    private Integer gender;

    @Schema(name = "租户ID")
    private String tenantId;


    public LoginUser convertLoginUser(String accountId){
        LoginUser loginUser = new LoginUser();
        BizAssert.isTrue("员工信息不能为空", Objects.isNull(this));
        loginUser.setUserId(this.getEmpId());
        loginUser.setAvatar(this.getAvatar());
        loginUser.setEmail(this.getEmail());
        loginUser.setPhone(this.getPhone());
        loginUser.setRealName(this.getRealName());
        loginUser.setTenantId(this.getTenantId());
        loginUser.setChannel(AccountChannelEnum.EMP.getValue());
        loginUser.setAccountId(accountId);
        return loginUser;
    }
}

