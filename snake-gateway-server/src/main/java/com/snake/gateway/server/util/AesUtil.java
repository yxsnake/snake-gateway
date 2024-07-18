package com.snake.gateway.server.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import io.github.yxsnake.pisces.web.core.exception.BizException;
import lombok.extern.slf4j.Slf4j;

/**
 * java使用AES加密解密 AES-128-ECB加密
 * 与mysql数据库aes加密算法通用
 * 数据库aes加密解密
 * -- 加密
 * SELECT to_base64(AES_ENCRYPT('www.gowhere.so','jkl;POIU1234++=='));
 * -- 解密
 * SELECT AES_DECRYPT(from_base64('Oa1NPBSarXrPH8wqSRhh3g=='),'jkl;POIU1234++==');
 * <p>
 * Cipher  AES加密为线程安全
 */
@Slf4j
public class AesUtil {

    public static AesInstance getInstance(String key) {
        return new AesInstance(key);
    }

    public static class AesInstance {
        private final Cipher cipherE;
        private final Cipher cipherD;

        private AesInstance(String key) {
            this(key, "AES/ECB/PKCS5Padding");
        }

        private AesInstance(String key, String type) {
            try {
                byte[] raw = key.getBytes(StandardCharsets.UTF_8);
                SecretKeySpec secretKeySpec = new SecretKeySpec(raw, "AES");
                //"算法/模式/补码方式"
                cipherD = Cipher.getInstance(type);
                cipherE = Cipher.getInstance(type);
                cipherD.init(Cipher.DECRYPT_MODE, secretKeySpec);
                cipherE.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            } catch (NoSuchPaddingException e) {
                log.error("AES加密初始化失败", e);
                throw new BizException("AES加密初始化失败");
            } catch (NoSuchAlgorithmException e) {
                log.error("AES加密初始化失败", e);
                throw new BizException("AES加密初始化失败");
            } catch (InvalidKeyException e) {
                log.error("AES加密初始化失败", e);
                throw new BizException("AES加密初始化失败");
            }
        }

        /**
         * 加密
         *
         * @param src
         * @return
         * @throws UnsupportedEncodingException
         * @throws BadPaddingException
         * @throws IllegalBlockSizeException
         */
        public String encrypt(String src) {
            try {
                byte[] encrypted = cipherE.doFinal(src.getBytes(StandardCharsets.UTF_8));
                //此处使用BASE64做转码功能，同时能起到2次加密的作用。
                return Base64Util.encryptBuffer(encrypted);
            } catch (Exception e) {
                log.error("加密失败", e);
                throw new BizException("AES加密失败");
            }
        }

        /**
         * 解密
         *
         * @param src
         * @return
         * @throws IOException
         * @throws BadPaddingException
         * @throws IllegalBlockSizeException
         */
        public String decrypt(String src) {
            try {
                //先用base64解密
                byte[] encrypted = Base64Util.decodeBuffer(src);
                byte[] origina = cipherD.doFinal(encrypted);
                return new String(origina, StandardCharsets.UTF_8);
            } catch (Exception e) {
                log.error("解密失败", e);
                throw new BizException("AES解密失败");
            }
        }
    }

}
