package com.snake.gateway.common.configure;

import com.snake.gateway.common.exception.IExceptionProcess;
import com.snake.gateway.common.filter.IndexFilter;
import com.snake.gateway.common.handler.HealthCheckHandler;
import com.snake.gateway.common.handler.TimeGatewayHandler;
import com.snake.gateway.common.handler.TimestampTokenCheckHandler;
import com.snake.gateway.common.interfaces.IGatewayHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import java.util.Comparator;
import java.util.List;

@Slf4j
@Import(ExceptionBeanConfigure.class)
public class GatewayConfigure {

    @Bean
    public IndexFilter indexFilter(List<IGatewayHandler> gatewayHandlerList, List<IExceptionProcess> exceptionProcessList) {
        gatewayHandlerList.sort(Comparator.comparing(IGatewayHandler::sort));
        log.info("IGatewayHandler load:");
        gatewayHandlerList.stream().forEach(g -> {
            log.info("----{}", g.getClass());
        });
        log.info("IExceptionProcess load:");
        exceptionProcessList.stream().forEach(g -> {
            log.info("----{}", g.getClass());
        });
        return new IndexFilter(gatewayHandlerList, exceptionProcessList);
    }

    @Bean
    public HealthCheckHandler healthCheckHandler() {
        return new HealthCheckHandler();
    }

    @Bean
    public TimeGatewayHandler timeGatewayHandler() {
        return new TimeGatewayHandler();
    }

    @Bean
    @ConditionalOnProperty(name = "gateway.handler.timestampCheck.enable", havingValue = "true")
    public TimestampTokenCheckHandler timestampTokenCheckHandler() {
        return new TimestampTokenCheckHandler();
    }
}
