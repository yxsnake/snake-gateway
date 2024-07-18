package com.snake.gateway.server.retrofit.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: snake
 * @create-time: 2024-07-04
 * @description: 资源权限传输对象
 * @version: 1.0
 */
@Schema(name = "资源权限传输对象")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResourceDTO {

    @Schema(name = "资源ID")
    private String resourceId;

    @Schema(name = "平台资源ID")
    private String pResourceId;

    @Schema(name = "上级资源ID")
    private String parentId;

    @Schema(name = "资源类型(0:目录,1:菜单, 2:按钮 ,3:外链,4:接口)")
    private Integer resourceType;

    @Schema(name = "资源名称")
    private String name;

    @Schema(name = "路由路径(浏览器地址栏路径)")
    private String path;

    @Schema(name = "组件路径(vue页面完整路径，省略.vue后缀)")
    private String component;

    @Schema(name = "按钮权限标识")
    private String perm;

    @Schema(name = "跳转路径")
    private String redirect;

    @Schema(name = "菜单图标")
    private String icon;

    @Schema(name = "备注")
    private String remark;

    @Schema(name = "排序")
    private Long sort;

    @Schema(name = "是否禁用（0-否，1-是）")
    private Integer disabled;

    @Schema(name = "是否删除（0-否，1-是）")
    private Integer deleted;

    @Schema(name = "层级（0～5， 最多支持 6 层）")
    private String level;

    @Schema(name = "租户唯一标识")
    private String tenantId;

}

