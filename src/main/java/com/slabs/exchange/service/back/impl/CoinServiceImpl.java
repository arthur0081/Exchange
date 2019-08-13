package com.slabs.exchange.service.back.impl;

import com.slabs.exchange.mapper.back.CoinMapper;
import com.slabs.exchange.model.common.ResponseBean;
import com.slabs.exchange.model.entity.Coin;
import com.slabs.exchange.service.back.ICoinService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CoinServiceImpl implements ICoinService {

    @Resource
    private CoinMapper coinMapper;


    /**
     * 得到所有的 USDT 和 HOS
     */
    @Override
    public ResponseBean getCoins() {
        List<Coin> list = coinMapper.getCoins();
        return new ResponseBean(200, "", list);
    }
}
