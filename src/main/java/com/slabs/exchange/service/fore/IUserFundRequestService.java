package com.slabs.exchange.service.fore;

import com.slabs.exchange.model.common.ResponseBean;
import com.slabs.exchange.model.dto.PageParamDto;

public interface IUserFundRequestService {
    ResponseBean list(PageParamDto pageParamDto);
}
