package com.snake.gateway.server.service.impl;

import com.snake.gateway.server.model.dto.MemberDTO;
import com.snake.gateway.server.model.dto.SupplierDTO;
import com.snake.gateway.server.retrofit.client.AccountClient;
import com.snake.gateway.server.retrofit.client.EmpClient;
import com.snake.gateway.server.retrofit.client.UserAuthClient;
import com.snake.gateway.server.retrofit.model.dto.AccountDTO;
import com.snake.gateway.server.retrofit.model.dto.EmpDTO;
import com.snake.gateway.server.retrofit.model.dto.ResourceDTO;
import com.snake.gateway.server.retrofit.model.enums.AccountChannelEnum;
import com.snake.gateway.server.retrofit.model.enums.AccountSupperAdminEnum;
import com.snake.gateway.server.retrofit.model.enums.ResourceTypeEnum;
import com.snake.gateway.server.retrofit.model.queries.UserResourceEqualsQueries;
import com.snake.gateway.server.service.UserService;
import io.github.yxsnake.pisces.web.core.base.LoginUser;
import io.github.yxsnake.pisces.web.core.base.Result;
import io.github.yxsnake.pisces.web.core.utils.BizAssert;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final EmpClient empClient;

    private final AccountClient accountClient;

    private final UserAuthClient userAuthClient;

    @Override
    public LoginUser getLoginUser(String accountId, String loginType) {
        LoginUser loginUser = null;
        AccountChannelEnum accountChannelEnum = AccountChannelEnum.getInstance(Integer.parseInt(loginType));
        if(AccountChannelEnum.EMP.equals(accountChannelEnum)){
            loginUser = this.getEmpByAccountId(accountId)
                    .convertLoginUser(accountId);
            Result<AccountDTO> dtoResult = accountClient.findByAccountId(accountId);
            if(Objects.nonNull(dtoResult) && Result.isSuccess(dtoResult)){
                AccountDTO accountDTO = dtoResult.getData();
                AccountSupperAdminEnum accountSupperAdminEnum = AccountSupperAdminEnum.getInstance(accountDTO.getSupperAdmin());
                loginUser.setSupperAdmin(AccountSupperAdminEnum.SUPPER.equals(accountSupperAdminEnum)?Boolean.TRUE:Boolean.FALSE);
            }
        }else if(AccountChannelEnum.MEMBER.equals(accountChannelEnum)){
            loginUser = this.getMemberByAccountId(accountId).convertLoginUser();
        }else if(AccountChannelEnum.SUPPLIER.equals(accountChannelEnum)){
            loginUser = this.getSupplierByAccountId(accountId).convertLoginUser();
        }else {
            BizAssert.isTrue("不支持的登录渠道",true);
        }
        return loginUser;
    }

    @Override
    public List<ResourceDTO> getAuthList(String accountId) {
        UserResourceEqualsQueries queries = new UserResourceEqualsQueries();
        queries.setAccountId(accountId);
        queries.setResourceType(ResourceTypeEnum.INTERFACE.getValue());
        Result<List<ResourceDTO>> result = userAuthClient.getResources(queries);
        if(Objects.nonNull(result) && Result.isSuccess(result)){
            return result.getData();
        }
        return new ArrayList<>();
    }

    private EmpDTO getEmpByAccountId(String accountId) {
        Result<EmpDTO> result = empClient.get(accountId);
        BizAssert.isTrue(result.getMsg(),!Result.isSuccess(result));
        return result.getData();
    }

    private MemberDTO getMemberByAccountId(String accountId) {
        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setAccountId("1");
        memberDTO.setMemberId("1");
        memberDTO.setEmail("370696614@qq.com");
        memberDTO.setNickName("张三");
        memberDTO.setPhone("18512341234");
        memberDTO.setTenantId("999999");
        memberDTO.setAccountId("1");
        memberDTO.setAvatar("https://i1.hdslb.com/bfs/face/98a570a6c6d6a263bcb0cba9e15e492125e9d310.jpg@120w_120h_1c");
        return memberDTO;
    }

    private SupplierDTO getSupplierByAccountId(String accountId) {
        SupplierDTO supplierDTO = new SupplierDTO();
        supplierDTO.setAccountId("1");
        supplierDTO.setSupplierId("1");
        supplierDTO.setEmail("370696614@qq.com");
        supplierDTO.setRealName("张三");
        supplierDTO.setPhone("18512341234");
        supplierDTO.setTenantId("999999");
        supplierDTO.setAccountId("1");
        supplierDTO.setAvatar("https://i1.hdslb.com/bfs/face/98a570a6c6d6a263bcb0cba9e15e492125e9d310.jpg@120w_120h_1c");
        return supplierDTO;
    }

}
