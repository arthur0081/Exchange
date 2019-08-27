package com.slabs.exchange.service.fore;


import com.slabs.exchange.model.common.ResponseBean;

public interface IWithdrawService {
    ResponseBean withdraw(WithdrawDto withdrawDto);
}
