package com.slabs.exchange.service.back;

import com.slabs.exchange.model.common.ResponseBean;
import com.slabs.exchange.model.dto.PageParamDto;

public interface ITradeService {
    ResponseBean list(PageParamDto pageParamDto);
}
