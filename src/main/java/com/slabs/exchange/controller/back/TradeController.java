package com.slabs.exchange.controller.back;


import com.slabs.exchange.model.common.ResponseBean;
import com.slabs.exchange.model.dto.PageParamDto;
import com.slabs.exchange.service.back.ITradeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 成交记录
 */
@RestController
@RequestMapping("trade")
@Api(value = "交易", description = "交易模块：交易相关接口")
public class TradeController {

    @Resource
    private ITradeService tradeService;


    /**
     * 交易记录的列表查询
     */
    @PostMapping("list")
    @ApiOperation(value = "列表")
    public ResponseBean list(@RequestBody PageParamDto pageParamDto) {
        return tradeService.list(pageParamDto);
    }

}
