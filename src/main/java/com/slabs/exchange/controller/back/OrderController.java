package com.slabs.exchange.controller.back;


import com.slabs.exchange.model.common.ResponseBean;
import com.slabs.exchange.model.dto.PageParamDto;
import com.slabs.exchange.model.dto.SymbolDto;
import com.slabs.exchange.service.back.IOrderService;
import com.slabs.exchange.service.back.ISymbolService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("order")
@Api(value = "订单列表", description = "订单：订单查询列表")
public class OrderController {

    @Resource
    private IOrderService orderService;

    /**
     * 列表查询
     */
    @ApiOperation(value = "列表查询")
    @PostMapping("list")
    public ResponseBean list(@RequestBody PageParamDto pageParamDto) {
        return orderService.list(pageParamDto);
    }
}
