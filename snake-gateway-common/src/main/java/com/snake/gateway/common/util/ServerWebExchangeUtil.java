package com.snake.gateway.common.util;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.snake.gateway.common.bean.GatewayContext;
import com.snake.gateway.common.interfaces.IBodyFilter;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.RequestPath;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

public class ServerWebExchangeUtil {

    /**
     * 通过网关上线文生成新的ServerWebExchange
     *
     * @param context
     * @return
     */
    public static ServerWebExchange buildServerWebExchange(GatewayContext context) {
        ServerWebExchange exchange = context.getExchange();
        ServerHttpRequest request = null;

        List<IBodyFilter> bodyFilterList = context.getBodyFilterList();
        if (!CollectionUtils.isEmpty(bodyFilterList) || context.headerOrPathChange()) {
            // 新建一个ServerHttpRequest装饰器,覆盖需要装饰的方法
            request = new ServerHttpRequestDecorator(context.getExchange().getRequest()) {

                @Override
                public RequestPath getPath() {
                    String path = context.getPath();
                    if (null == path) {
                        return super.getPath();
                    }
                    URI uri = getUriToUse(exchange.getRequest().getURI(), path);
                    RequestPath.parse(uri, exchange.getRequest().getPath().contextPath().value());
                    return super.getPath();
                }

                @Override
                public HttpHeaders getHeaders() {
                    if (!CollectionUtils.isEmpty(bodyFilterList)) {
                        HttpHeaders httpHeaders = new HttpHeaders();
                        super.getHeaders().entrySet().forEach(e -> {
                            if (!"content-length".equalsIgnoreCase(e.getKey())) {
                                httpHeaders.addAll(e.getKey(), e.getValue());
                            }
                        });
                        return httpHeaders;
                    }
                    HttpHeaders header = context.getHeader();
                    if (null != header) {
                        return header;
                    }
                    return super.getHeaders();
                }

                @Override
                public Flux<DataBuffer> getBody() {
                    if (CollectionUtils.isEmpty(bodyFilterList)) {
                        return super.getBody();
                    }
                    Flux<DataBuffer> body = exchange.getRequest().getBody();
                    return body.collectList().flatMapIterable(l -> {
                        String str = dataBufferListToString(l);
                        JSONObject json = JSON.parseObject(str);
                        for (IBodyFilter filter : bodyFilterList) {
                            json = filter.filter(json);
                        }

                        return Arrays.asList(exchange.getResponse().bufferFactory()
                                .wrap(json.toJSONString().getBytes(StandardCharsets.UTF_8)));
                    });
                }
            };
            return exchange.mutate().request(request).build();
        }
        return exchange;
    }

    private static String dataBufferListToString(List<DataBuffer> list) {
        StringBuilder sb = new StringBuilder();
        list.forEach(buffer -> {
            byte[] bs = new byte[buffer.readableByteCount()];
            buffer.read(bs);
            //释放掉内存
            DataBufferUtils.release(buffer);
            sb.append(new String(bs, StandardCharsets.UTF_8));
        });
        return sb.toString();
    }

    private static URI getUriToUse(URI uri, String uriPath) {
        StringBuilder uriBuilder = new StringBuilder();
        if (uri.getScheme() != null) {
            uriBuilder.append(uri.getScheme()).append(':');
        }
        if (uri.getRawUserInfo() != null || uri.getHost() != null) {
            uriBuilder.append("//");
            if (uri.getRawUserInfo() != null) {
                uriBuilder.append(uri.getRawUserInfo()).append('@');
            }
            if (uri.getHost() != null) {
                uriBuilder.append(uri.getHost());
            }
            if (uri.getPort() != -1) {
                uriBuilder.append(':').append(uri.getPort());
            }
        }
        if (StringUtils.hasLength(uriPath)) {
            uriBuilder.append(uriPath);
        }
        if (uri.getRawQuery() != null) {
            uriBuilder.append('?').append(uri.getRawQuery());
        }
        if (uri.getRawFragment() != null) {
            uriBuilder.append('#').append(uri.getRawFragment());
        }
        try {
            return new URI(uriBuilder.toString());
        } catch (URISyntaxException ex) {
            throw new IllegalStateException("Invalid URI path: \"" + uriPath + "\"", ex);
        }
    }
}
