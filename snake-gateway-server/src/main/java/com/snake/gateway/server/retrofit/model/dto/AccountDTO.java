package com.snake.gateway.server.retrofit.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.github.yxsnake.pisces.web.core.base.BaseEntity;
import io.github.yxsnake.pisces.web.core.converter.Convert;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author: snake
 * @create-time: 2024-06-26
 * @description: 账号信息传输对象
 * @version: 1.0
 */
@Data
@Schema(name = "账号信息传输对象")
public class AccountDTO extends BaseEntity implements Convert, Serializable {

    @Schema(name = "账号ID")
    private String accountId;

    @Schema(name = "用户ID(员工 ID/会员 ID/供应商 ID,由渠道决定)")
    private String userId;

    @Schema(name = "账号")
    private String account;

    @Schema(name = "密码")
    private String password;

    @Schema(name = "账号归属渠道(11-员工，12-会员，13-供应商）")
    private Integer channel;

    @Schema(name = "登录方式(1-密码登录，2-手机短信验证码登录，3-扫码登录,4-微信授权登录，5-小程序授权登录)")
    private Integer loginWay;

    @Schema(name = "三方授权ID")
    private String openId;

    @Schema(name = "租户ID")
    private String tenantId;

    @Schema(name = "是否默认租户(0-否，1-是）")
    private Integer defaultTenantFlag;

    @Schema(name = "账号是否禁用(0-否，1-是）")
    private Integer disabled;

    @Schema(name = "是否超级管理员(0-否，1-是）")
    private Integer supperAdmin;

    @Schema(name = "最后登录时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lastLoginTime;

    @Schema(name = "最后登录IP")
    private String lastLoginIp;

}

