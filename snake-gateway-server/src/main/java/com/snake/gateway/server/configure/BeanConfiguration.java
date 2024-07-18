package com.snake.gateway.server.configure;

import com.snake.gateway.common.conditional.ConditionalOnPropertyNotEmpty;
import com.snake.gateway.common.handler.CorsAddHandler;
import com.snake.gateway.common.handler.CorsRemoveHandler;
import com.snake.gateway.common.handler.PathSkipHandler;
import com.snake.gateway.common.handler.match.PathSkipFullMatching;
import com.snake.gateway.common.interfaces.match.IPathSkipMatch;
import com.snake.gateway.server.bean.CustomServer;
import com.snake.gateway.server.bean.K8sServer;
import com.snake.gateway.server.configure.properties.SnakeGatewayProperties;
import com.snake.gateway.server.handler.K8sDnsRouterGatewayFilterFactory;
import com.snake.gateway.server.handler.LocalRouterGatewayFilterFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.gateway.filter.factory.GatewayFilterFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
public class BeanConfiguration {

    @Bean
    @ConditionalOnProperty(name = "gateway.cors.remove.enable", havingValue = "true")
    public CorsRemoveHandler corsRemoveHandler() {
        return new CorsRemoveHandler();
    }

    @Bean
    @ConditionalOnProperty(name = "gateway.cors.add.enable", havingValue = "true")
    public CorsAddHandler corsAddHandler() {
        return new CorsAddHandler();
    }

    @Bean
    @ConditionalOnPropertyNotEmpty(key = "gateway.path.skip.fullMatching")
    public PathSkipFullMatching pathSkipFullMatching() {
        return new PathSkipFullMatching();
    }

    @Bean
    @ConditionalOnBean(IPathSkipMatch.class)
    public PathSkipHandler pathSkipHandler(List<IPathSkipMatch> list) {
        return new PathSkipHandler(list);
    }

    @Bean
    public GatewayFilterFactory k8sDnsRouterGatewayFilterFactory(
            SnakeGatewayProperties gatewayProperties) {
        List<K8sServer> k8sServers = gatewayProperties.getK8sServers();
        List<CustomServer> customServers = gatewayProperties.getCustomServers();
        Map<String, K8sServer> k8sServerMap = Collections.emptyMap();
        Map<String, CustomServer> customServerMap = Collections.emptyMap();
        if (!CollectionUtils.isEmpty(k8sServers)) {
            k8sServerMap = k8sServers.stream()
                    .collect(Collectors.toMap(K8sServer::getApplicationName, k -> k));
        }
        if (!CollectionUtils.isEmpty(customServers)) {
            customServerMap = customServers.stream()
                    .collect(Collectors.toMap(CustomServer::getApplicationName, k -> k));
        }
        return new K8sDnsRouterGatewayFilterFactory(k8sServerMap, customServerMap);
    }

    @Bean
    public GatewayFilterFactory localRouterGatewayFilterFactory() {
        return new LocalRouterGatewayFilterFactory();
    }

    /**
     * RedisTemplate配置
     *
     * @param factory
     * @return
     */
    @Bean
    public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory factory) {
        StringRedisTemplate template = new StringRedisTemplate(factory);
        template.afterPropertiesSet();
        return template;
    }
}
