package com.slabs.exchange.service.fore;


import com.slabs.exchange.model.common.ResponseBean;
import com.slabs.exchange.model.dto.WithdrawDto;

public interface IWithdrawService {
    ResponseBean withdraw(WithdrawDto withdrawDto);
}
