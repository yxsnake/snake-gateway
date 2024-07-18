package com.snake.gateway.server.configure.properties;

import com.snake.gateway.server.bean.CustomServer;
import com.snake.gateway.server.bean.K8sServer;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Data
@Configuration
@ConfigurationProperties("gateway")
public class SnakeGatewayProperties {

    private List<K8sServer> k8sServers;

    private List<CustomServer> customServers;
}
