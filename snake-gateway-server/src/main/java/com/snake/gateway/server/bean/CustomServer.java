package com.snake.gateway.server.bean;

import lombok.Data;

@Data
public class CustomServer {

    private String applicationName;

    private String host;

    private Integer port;

    private String path;

}
