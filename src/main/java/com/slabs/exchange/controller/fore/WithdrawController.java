package com.slabs.exchange.controller.fore;


import com.slabs.exchange.model.common.ResponseBean;
import com.slabs.exchange.model.dto.WithdrawDto;
import com.slabs.exchange.service.fore.IWithdrawService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


/**
 * 前台： 提现  控制器
 */
@RestController
@RequestMapping("fore")
public class WithdrawController {
    @Resource
    private IWithdrawService withdrawService;

    /**
     * 提现记录列表
     */
    @PostMapping("withdraw")
    public ResponseBean withdraw(@RequestBody WithdrawDto withdrawDto) {
        return withdrawService.withdraw(withdrawDto);
    }

}
