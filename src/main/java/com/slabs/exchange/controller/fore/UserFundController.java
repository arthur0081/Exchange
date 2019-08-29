package com.slabs.exchange.controller.fore;

import com.slabs.exchange.model.common.ResponseBean;
import com.slabs.exchange.model.dto.WithdrawDto;
import com.slabs.exchange.service.fore.IUserFundService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 用户资金管理表
 */
@RestController
@RequestMapping("user-fund")
@Api(value = "用户资金管理", description = "用户资金管理：用户资金管理相关接口")
public class UserFundController {
    @Resource
    private IUserFundService userFundService;

    /**
     * 用户所有的资金列表展示
     */
    @PostMapping("list")
    public ResponseBean list() {
        return userFundService.list();
    }

    /**
     * 充值（给当前登陆者返回用户的钱包地址）（充值对于后台：就这一个功能）
     */
    @PostMapping("get-wallet-addr")
    public ResponseBean getWalletAddr() {
        return userFundService.getWalletAddr();
    }

    /**
     * 提现通过调用黄奕的提现接口，而黄毅的接口都是直接修改数据库表的。
     */

    /**
     * 提现（给当前登陆者返回用户的钱包地址）
     */
    @PostMapping("withdraw")
    public ResponseBean withdraw(WithdrawDto withdrawDto) {
        return userFundService.withdraw(withdrawDto);
    }

}
