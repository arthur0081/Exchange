package com.slabs.exchange.controller.back;

import com.slabs.exchange.model.common.ResponseBean;
import com.slabs.exchange.service.back.ICoinService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


/**
 * USDT 和 HOS
 */
@RestController
@RequestMapping("coin")
public class CoinController {

    @Resource
    private ICoinService coinService;

    /**
     * 查询所有的USDT 和  HOS
     */
    @PostMapping("getCoins")
    public ResponseBean getCoins() {
        return coinService.getCoins();
    }
}
