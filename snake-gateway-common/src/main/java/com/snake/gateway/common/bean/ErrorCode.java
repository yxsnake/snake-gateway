package com.snake.gateway.common.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * 业务异常类
 *
 * @author snake
 */
@Getter
@ToString
@Builder
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
public class ErrorCode {

    /**
     * 错误码
     */
    private Integer code;
    /**
     * 错误消息
     */
    private String msg;

    public static final Integer BIZ_ERROR_CODE = 400;

    public static final ErrorCode SYSTEM_ERROR = ErrorCode.builder().code(500).msg("系统错误").build();

}
