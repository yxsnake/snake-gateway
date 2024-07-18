package com.snake.gateway.server.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;

import io.github.yxsnake.pisces.web.core.exception.BizException;
import lombok.extern.slf4j.Slf4j;

/**
 * Decoder
 * Encoder
 * 是线程安全的
 */
@Slf4j
public class Base64Util {

    public static Decoder base64Decoder = Base64.getDecoder();
    public static Encoder base64Encoder = Base64.getEncoder();


    public static Base64Instance getInstance() {
        return new Base64Instance();
    }

    /**
     * 加密
     */
    public static String encrypt(String src) throws UnsupportedEncodingException {
        return encryptBuffer(src.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 加密
     */
    public static String encryptBuffer(byte[] bytes) {
        return base64Encoder.encodeToString(bytes);
    }

    /**
     * 解密
     */
    public static String decrypt(String src) throws IOException {
        return new String(decodeBuffer(src), StandardCharsets.UTF_8);
    }

    /**
     * 解密
     */
    public static byte[] decodeBuffer(String str) {
        return base64Decoder.decode(str);
    }

    public static class Base64Instance {

        public String encrypt(String src) {
            try {
                return Base64Util.encrypt(src);
            } catch (Exception e) {
                log.error("加密失败", e);
                throw new BizException("Base64加密失败");
            }
        }

        public String decrypt(String src) {
            try {
                return Base64Util.decrypt(src);
            } catch (Exception e) {
                log.error("解密失败", e);
                throw new BizException("Base64解密失败");
            }
        }
    }

}
