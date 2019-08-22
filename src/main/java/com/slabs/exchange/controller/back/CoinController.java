package com.slabs.exchange.controller.back;

import com.slabs.exchange.model.common.ResponseBean;
import com.slabs.exchange.model.dto.CoinDto;
import com.slabs.exchange.service.back.ICoinService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("coin")
@Api(value = "项目币", description = "项目币：项目币新增功能")
public class CoinController {

    @Resource
    private ICoinService coinService;

    /**
     * 新增币种（新增的时候去发币）HOS是手工部署
     */
    @PostMapping("insert")
    public ResponseBean insert(@RequestBody CoinDto coinDto) {
        return coinService.insert(coinDto);
    }

    /**
     * 获取  USDT 和  HOS
     */
    @PostMapping("getCoins")
    public ResponseBean getCoins() {
        return coinService.getCoins();
    }

    /**
     *  获取没有关联币对的项目币
     */
    @PostMapping("get-nonsymbol-coin")
    @ApiOperation(value = "获取没有关联币对的项目币")
    public ResponseBean getNonsymbolCoin() {
        return coinService.getNonsymbolCoin();
    }

    /**
     *  获取没有关联币对的项目币
     */
    @PostMapping("get-all-coins")
    @ApiOperation(value = "获取所有币种")
    public ResponseBean getAllCoins() {
        return coinService.getAllCoins();
    }
}
