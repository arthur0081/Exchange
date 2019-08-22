package com.slabs.exchange.service.back;

import com.slabs.exchange.model.common.ResponseBean;
import com.slabs.exchange.model.dto.CoinDto;

public interface ICoinService {
    ResponseBean getCoins();

    ResponseBean insert(CoinDto coinDto);

    ResponseBean getNonsymbolCoin();

    ResponseBean getAllCoins();
}
