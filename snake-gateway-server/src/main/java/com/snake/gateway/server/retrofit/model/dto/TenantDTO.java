package com.snake.gateway.server.retrofit.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "租户信息")
public class TenantDTO {

    private String tenantId;

    private String tenantName;

    private String introduction;

    private String supperPerson;

    private String email;

    private String phone;

    private String supperAccount;
}