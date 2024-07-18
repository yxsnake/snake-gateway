package com.snake.gateway.common.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class MD5Util {


    /**
     * 把字节数组转成16进位制数
     *
     * @param bytes
     * @return
     */
    public static String bytesToHex(byte[] bytes) {
        StringBuffer md5str = new StringBuffer();
        //把数组每一字节换成16进制连成md5字符串
        int digital;
        for (int i = 0; i < bytes.length; i++) {
            digital = bytes[i];
            if (digital < 0) {
                digital += 256;
            }
            if (digital < 16) {
                md5str.append("0");
            }
            md5str.append(Integer.toHexString(digital));
        }
        return md5str.toString().toLowerCase();
    }

    /**
     * 把字节数组转换成md5
     *
     * @param input
     * @return
     */
    public static String bytesToMD5(byte[] input, int time) {
        String md5str = null;
        try {
            //创建一个提供信息摘要算法的对象，初始化为md5算法对象
            MessageDigest md = MessageDigest.getInstance("MD5");
            while (time > 0) {
                //计算后获得字节数组
                input = md.digest(input);
                time--;
            }
            //把数组每一字节换成16进制连成md5字符串
            md5str = bytesToHex(input);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return md5str;
    }

    /**
     * 把字符串转换成md5
     *
     * @param str  加密字符串ss
     * @param time 次数
     * @return
     */
    public static String strToMD5(String str, int time) {
        return bytesToMD5(str.getBytes(StandardCharsets.UTF_8), time);
    }

    /**
     * 把字符串转成16进位制数
     * 循环此方法和strToMD5(str, n)加密多次返回结果不同
     *
     * @param str 加密字符串
     * @return
     */
    public static String strToHex(String str) {
        return strToMD5(str, 1);
    }
}
