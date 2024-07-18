package com.snake.gateway.server.configure.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
@ConfigurationProperties(prefix = "user-auth")
public class UserAuthProperties {

    private List<String> excludePaths;
}
