package com.snake.gateway.common.bean;

import com.snake.gateway.common.enums.GatewayActionEnum;
import com.snake.gateway.common.interfaces.IBodyFilter;
import io.github.yxsnake.pisces.web.core.base.Result;
import org.springframework.http.HttpHeaders;
import org.springframework.web.server.ServerWebExchange;

import java.util.LinkedList;
import java.util.List;

public class GatewayContext {
    private ServerWebExchange exchange;
    private Result result;
    private String redirectUrl;
    private List<IBodyFilter> bodyFilterList;

    private String path;
    private HttpHeaders header;

    public GatewayContext(ServerWebExchange exchange) {
        this.exchange = exchange;
    }

    public <T> T getAttribute(String key) {
        return exchange.getAttribute(key);
    }

    public void putAttribute(String key, Object obj) {
        exchange.getAttributes().put(key, obj);
    }

    public GatewayActionEnum result(Result result) {
        this.result = result;
        return GatewayActionEnum.RESULT;
    }

    public ServerWebExchange getExchange() {
        return exchange;
    }

    public GatewayContext setExchange(ServerWebExchange exchange) {
        this.exchange = exchange;
        return this;
    }

    public Result getResult() {
        return result;
    }

    public GatewayContext setResult(Result result) {
        this.result = result;
        return this;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public GatewayContext setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
        return this;
    }

    public String getPath() {
        if (null == path) {
            path = exchange.getRequest().getURI().getPath();
        }
        return path;
    }

    public GatewayContext setPath(String path) {
        this.path = path;
        return this;
    }

    public void addHeader(String key, String value) {
        if (null == this.header) {
            this.header = new HttpHeaders();
        }
        this.header.add(key, value);
    }

    public HttpHeaders getHeader() {
        return header;
    }

    public boolean headerOrPathChange() {
        return null != header || null != path;
    }

    public void addBodyFilter(IBodyFilter bodyFilter) {
        if (null == bodyFilterList) {
            bodyFilterList = new LinkedList<>();
        }
        bodyFilterList.add(bodyFilter);
    }

    public List<IBodyFilter> getBodyFilterList() {
        return bodyFilterList;
    }
}
