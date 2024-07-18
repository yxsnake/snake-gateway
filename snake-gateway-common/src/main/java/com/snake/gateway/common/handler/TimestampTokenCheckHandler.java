package com.snake.gateway.common.handler;

import com.snake.gateway.common.bean.GatewayContext;
import com.snake.gateway.common.cons.GatewayCons;
import com.snake.gateway.common.cons.GatewaySortCons;
import com.snake.gateway.common.enums.GatewayActionEnum;
import com.snake.gateway.common.interfaces.IGatewayHandler;
import com.snake.gateway.common.util.MD5Util;
import io.github.yxsnake.pisces.web.core.utils.BizAssert;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;

@Slf4j
public class TimestampTokenCheckHandler implements IGatewayHandler {
    @Value("${gateway.handler.timestampCheck.timestamp:timestamp}")
    private String timestamp;
    @Value("${gateway.handler.timestampCheck.timestamp:t1}")
    private String timestampCheck;
    @Value("${gateway.handler.timestampCheck.mod:3}")
    private int mod;
    @Value("${gateway.handler.timestampCheck.expire:5000}")
    private long expire;
    @Value("${gateway.handler.timestampCheck.checkTimestamp:false}")
    private Boolean checkTimestamp;
    @Value("${gateway.handler.timestampCheck.testTimestamp:skip}")
    private String testTimestamp;

    @Override
    public GatewayActionEnum before(GatewayContext context) {
        String token = context.getAttribute(GatewayCons.ATTRIBUTE_TOKEN);
        if (StringUtils.isEmpty(token)) {
            log.debug("TimestampTokenCheckHandler 跳过处理,未获取token信息");
            return GatewayActionEnum.CONTINUE;
        }
        String timestampHeader = context.getExchange().getRequest().getHeaders()
                .getFirst(timestamp);
        String timestampCheckHeader = context.getExchange().getRequest().getHeaders()
                .getFirst(timestampCheck);
        BizAssert.isTrue("校验失败-001",Long.valueOf(timestampHeader)-Long.valueOf(timestampCheck)>30*1000);
        if (!StringUtils.isEmpty(testTimestamp)) {
            if (testTimestamp.equals(timestampHeader)) {
                log.debug("TimestampTokenCheckHandler 跳过处理,测试请求");
                return GatewayActionEnum.CONTINUE;
            }
        }

        //TODO: 兼容token前缀 后期删除
        if (token.startsWith("Bearer ")) {
            String encryption = encryption(timestampHeader, token.substring(7));
            if (encryption.equals(timestampCheckHeader)) {
                return GatewayActionEnum.CONTINUE;
            }
        }
        //TODO: 兼容token前缀 后期删除


        String encryption = encryption(timestampHeader, token);
        BizAssert.isTrue("校验失败-002",!encryption.equals(timestampCheckHeader));

        return GatewayActionEnum.CONTINUE;
    }

    private String encryption(String timestampHeader, String token) {
        Long time = Long.valueOf(timestampHeader);
        // MD5加密次数
        int md5Time = (int) (time % mod) + 1;

        String s1 = timestampHeader;
        String s2 = token;
        for (int i = 0; i < md5Time; i++) {
            s1 = MD5Util.strToHex(s1);
            s2 = MD5Util.strToHex(s2);
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s1.length() && i < s2.length(); i++) {
            sb.append(s1.charAt(i))
                    .append(s2.charAt(i));
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        String timestampHeader = "1636538371541";
        Long time = Long.valueOf(timestampHeader);
        // MD5加密次数
        int md5Time = (int) (time % 3) + 1;

        String s1 = timestampHeader;
        String s2 = "eyJhbGciOiJIUzI1NiJ9.eyJyIjoxLCJjIjoiMTEiLCJ0IjoiOTk5OTk5OTk5IiwidSI6IjEzOTA1NzEzNTE4NTg5ODcwMDgiLCJkb21haW4iOiJxZGluZ25ldC5jb20iLCJuIjoi5rWL6K-VMDAwMSIsImV4cCI6MTYzNzgzNDM2MX0.Sw1yIHJWw2_liexWQy4dIblDehqy35RwB3vt9myMHXk";
        for (int i = 0; i < md5Time; i++) {
            s1 = MD5Util.strToHex(s1);
            s2 = MD5Util.strToHex(s2);
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s1.length() && i < s2.length(); i++) {
            sb.append(s1.charAt(i))
                    .append(s2.charAt(i));
        }
        System.out.println(sb);
    }

    @Override
    public int sort() {
        return GatewaySortCons.HANDLER_TIMESTAMP_TOKEN_CHECK;
    }
}
