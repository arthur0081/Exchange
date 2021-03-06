package com.slabs.exchange.controller.back;


import com.slabs.exchange.model.common.ResponseBean;
import com.slabs.exchange.model.dto.PageParamDto;
import com.slabs.exchange.service.back.IBackIndexService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 后台：首页的逻辑
 */
@RestController
@RequestMapping("back")
@Api(value = "后台首页", description = "后台首页模块：后台首页相关接口")
public class BackIndexController {
    @Resource
    private IBackIndexService backIndexService;

    /**
     * 后台首页逻辑
     */
    @PostMapping("index")
    @ApiOperation(value = "首页")
    public ResponseBean getBackIndexInfo() {
        return backIndexService.getBackIndexInfo();
    }


    /**
     * 项目（币种）情况概览 列表
     */
    @PostMapping("coin-condition")
    @ApiOperation(value = "项目（币种）情况概览 列表")
    public ResponseBean getCoinConditionList(@RequestBody PageParamDto pageParamDto) {
        return backIndexService.getCoinConditionList(pageParamDto);
    }

    /**
     * 持币用户 列表
     */
    @PostMapping("hold-coin")
    @ApiOperation(value = "持币用户 列表")
    public ResponseBean getHoldCoinList(@RequestBody PageParamDto pageParamDto) {
        return backIndexService.getHoldCoinList(pageParamDto);
    }
}
