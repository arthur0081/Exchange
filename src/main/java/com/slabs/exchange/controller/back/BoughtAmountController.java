package com.slabs.exchange.controller.back;


import com.slabs.exchange.model.common.ResponseBean;
import com.slabs.exchange.model.dto.CoinDto;
import com.slabs.exchange.model.dto.PageParamDto;
import com.slabs.exchange.service.back.IBoughtAmountService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("buy")
@Api(value = "认购记录", description = "认购记录模块：认购记录相关接口")
public class BoughtAmountController {

    @Resource
    private IBoughtAmountService boughtAmountService;

    @PostMapping("list")
    public ResponseBean list(@RequestBody PageParamDto pageParamDto) {
        return boughtAmountService.list(pageParamDto);
    }
}
