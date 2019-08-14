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

}
