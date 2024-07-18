package com.snake.gateway.server.service;


import com.snake.gateway.server.retrofit.model.dto.ResourceDTO;
import io.github.yxsnake.pisces.web.core.base.LoginUser;

import java.util.List;


public interface UserService {
    /**
     * 获取用户信息
     * @param accountId
     * @param loginType
     * @return
     */
    LoginUser getLoginUser(String accountId, String loginType);

    /**
     * 查询当前用户的资源权限
     * @param accountId
     * @return
     */
    List<ResourceDTO> getAuthList(String accountId);
}
