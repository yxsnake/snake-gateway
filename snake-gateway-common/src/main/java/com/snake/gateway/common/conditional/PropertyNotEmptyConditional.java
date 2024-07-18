package com.snake.gateway.common.conditional;

import java.util.Map;

import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.util.StringUtils;

public class PropertyNotEmptyConditional extends SpringBootCondition {

    @Override
    public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata) {
        Map<String, Object> annotationAttributes = metadata.getAnnotationAttributes(ConditionalOnPropertyNotEmpty.class.getName());
        Object key = annotationAttributes.get("key");
        if (key == null) {
            return new ConditionOutcome(false, "error");
        }

        //获取environment中的值
        String value = context.getEnvironment().getProperty(key.toString());
        if (StringUtils.isEmpty(value)) {
            return new ConditionOutcome(false, "error");
        }
        //如果environment中的值与指定的value一致，则返回true
        return new ConditionOutcome(true, "ok");

    }
}
