package com.snake.gateway.server.service.impl;

import cn.dev33.satoken.stp.StpInterface;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONUtil;
import com.google.common.collect.Lists;
import com.snake.gateway.server.retrofit.client.UserAuthClient;
import com.snake.gateway.server.retrofit.model.queries.SsoPermissionQueries;
import com.snake.gateway.server.retrofit.model.queries.SsoRoleQueries;
import io.github.yxsnake.pisces.web.core.base.Result;
import io.github.yxsnake.pisces.web.core.utils.BizAssert;
import io.github.yxsnake.pisces.web.core.utils.JsonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class StpInterfaceImpl implements StpInterface {

    private final UserAuthClient userAuthClient;

    /**
     * 获取当前用户的权限标识集合
     *
     * @param loginId   账号id
     * @param loginType 账号类型
     * @return
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        SsoPermissionQueries queries = new SsoPermissionQueries();
        queries.setAccountId(String.valueOf(loginId));
        queries.setLoginType(loginType);
        Result<List<String>> result = userAuthClient.getPermissions(queries);
        BizAssert.isTrue("获取权限失败",!Result.isSuccess(result));
        return CollUtil.isNotEmpty(result.getData())?result.getData(): Lists.newArrayList();
    }

    /**
     * 获取当前用户的角色编码集合
     *
     * @param loginId   账号id
     * @param loginType 账号类型
     * @return
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        SsoRoleQueries queries = new SsoRoleQueries();
        queries.setAccountId(String.valueOf(loginId));
        queries.setLoginType(loginType);
        Result<List<String>> result = userAuthClient.getRoles(queries);
        BizAssert.isTrue("获取角色失败",!Result.isSuccess(result));
        return CollUtil.isNotEmpty(result.getData())?result.getData(): Lists.newArrayList();
    }


    private Map<String, String> headers() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json; charset=utf-8");
        return headers;
    }

    private String remoteCall(String url, Map<String, Object> paramMap) {
        String resultJson = HttpRequest.post(url)
                .headerMap(this.headers(), false)
                .body(JsonUtils.objectCovertToJson(paramMap))
                .timeout(5000)
                .execute().body();
        if (StrUtil.isNotBlank(resultJson)) {
            return JSONUtil.toBean(resultJson, new TypeReference<>() {
            }, true);
        }
        return null;
    }
}
