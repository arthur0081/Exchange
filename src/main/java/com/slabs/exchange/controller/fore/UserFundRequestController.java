package com.slabs.exchange.controller.fore;

import com.slabs.exchange.model.common.ResponseBean;
import com.slabs.exchange.model.dto.PageParamDto;
import com.slabs.exchange.service.fore.IUserFundRequestService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 用户资金提现充值成功表
 */
@RestController
@RequestMapping("fund-request")
@Api(value = "用户资金提现充值成功", description = "用户资金提现充值成功：用户资金提现充值成功相关接口")
public class UserFundRequestController {

    @Resource
    private IUserFundRequestService userFundRequestService;

    /**
     * 充提记录列表
     */
    @PostMapping("list")
    public ResponseBean list(@RequestBody PageParamDto pageParamDto) {
        return userFundRequestService.list(pageParamDto);
    }
}
