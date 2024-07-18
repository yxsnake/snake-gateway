package com.snake.gateway.server.bean;

import lombok.Data;

/**
 * k8s环境相关服务器
 */
@Data
public class K8sServer {
    private String applicationName;
    private String host;
    private Integer port;
}