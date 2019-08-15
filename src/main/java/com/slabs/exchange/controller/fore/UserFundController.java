package com.slabs.exchange.controller.fore;

import com.slabs.exchange.model.common.ResponseBean;
import com.slabs.exchange.service.fore.IUserFundService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 用户资金管理表
 */
@RestController
@RequestMapping("user-fund")
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
     * 充值（给当前登陆者返回用户的钱包地址）
     */
    @PostMapping("get-wallet-addr")
    public ResponseBean getWalletAddr() {
        return userFundService.getWalletAddr();
    }

    /**
     * 充值和提现 都是通过 钱包 调用 黄奕提供的接口，而黄毅的接口都是直接修改数据库表的。
     */
}
