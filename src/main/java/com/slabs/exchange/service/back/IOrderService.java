package com.slabs.exchange.service.back;

import com.slabs.exchange.model.common.ResponseBean;
import com.slabs.exchange.model.dto.PageParamDto;

public interface IOrderService {
    ResponseBean list(PageParamDto pageParamDto);
}
