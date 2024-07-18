package com.snake.gateway.common.interfaces;

import com.alibaba.fastjson2.JSONObject;
import org.springframework.core.Ordered;

public interface IBodyFilter extends Ordered {
    JSONObject filter(JSONObject jsonObject);

    @Override
    default int getOrder() {
        return 0;
    }
}