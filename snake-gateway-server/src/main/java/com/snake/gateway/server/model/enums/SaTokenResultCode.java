package com.snake.gateway.server.model.enums;

import io.github.yxsnake.pisces.web.core.base.IResultCode;
import lombok.Getter;

import java.io.Serializable;
import java.util.Arrays;

@Getter
public enum SaTokenResultCode implements IResultCode, Serializable {

    FAILED_TO_READ_VALID_TOKEN(11001, "未能读取到有效Token"),

    LOGIN_ACCOUNT_IS_EMPTY(11002, "登录时的账号为空"),

    INVALID_READ_TO_TOKEN(11011, "读取到Token无效"),

    INVALID_TOKEN(11012, "Token无效"),

    TOKEN_HAS_EXPIRED(11013, "Token已过期"),

    TOKEN_HAS_BEEN_DEACTIVATED(11014, "Token已被顶下线"),

    TOKEN_HAS_BEEN_KICKED_OFF_THE_LINE(11015, "Token已被踢下线"),

    TOKEN_HAS_BEEN_FROZEN(11016, "Token已被冻结"),

    THE_TOKEN_WAS_NOT_SUBMITTED_ACCORDING_TO_THE_SPECIFIED_PREFIX(11017, "未按照指定前缀提交token"),

    MISSING_SPECIFIED_ROLE(11041, "缺少指定的角色"),

    LACK_OF_SPECIFIED_PERMISSIONS(11051, "缺少指定的角色"),

    THE_CURRENT_ACCOUNT_FAILED_TO_PASS_THE_SERVICE_BAN_VERIFICATION(11061, "当前账号未通过服务封禁校验"),

    THE_ACCOUNT_PROVIDED_TO_LIFT_THE_BAN_IS_INVALID(11062, "提供要解禁的账号无效"),

    THE_SPECIFIED_PARAMETER_IS_MISSING_IN_THE_REQUEST(12001, "请求中缺少指定的参数"),

    PASSWORD_MD5_ENCRYPTION_EXCEPTION(12111, "密码md5加密异常"),

    FAILED_TO_PARSE_JWT_STRING(30201, "对jwt字符串解析失败"),

    THE_SIGNATURE_OF_THIS_JWT_IS_INVALID(30202, "此jwt的签名无效"),

    THE_LOGINTYPE_FIELD_OF_THIS_JWT_DOES_NOT_MEET_EXPECTATIONS(30203, "此jwt的loginType字段不符合预期"),

    THIS_JWT_HAS_TIMED_OUT(30204, "此jwt已超时"),

    NO_JWT_KEY_CONFIGURED(30205, "没有配置jwt秘钥"),

    THE_ACCOUNT_NUMBER_PROVIDED_WHEN_LOGGING_IN_IS_EMPTY(30206, "登录时提供的账号为空"),

    ;


    private final Integer code;

    private final String msg;

    SaTokenResultCode(final Integer code, final String msg) {
        this.code = code;
        this.msg = msg;
    }


    public static SaTokenResultCode getInstance(final String code) {
        return Arrays.asList(values()).stream().filter(item -> item.getCode().equals(code)).findFirst().orElse(null);
    }
}
