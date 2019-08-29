package com.slabs.exchange.service.fore;

import com.slabs.exchange.model.common.ResponseBean;
import com.slabs.exchange.model.dto.WithdrawDto;

public interface IUserFundService {
    ResponseBean list();

    ResponseBean getWalletAddr();

    ResponseBean withdraw(WithdrawDto withdrawDto);
}
