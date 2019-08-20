package com.slabs.exchange.service.back;

import com.slabs.exchange.model.common.ResponseBean;
import com.slabs.exchange.model.dto.PageParamDto;
import com.slabs.exchange.model.dto.ProjectCoinDto;

public interface IProjectCoinService {
    ResponseBean insert(ProjectCoinDto projectCoinDto);

    ResponseBean preUpdate(Integer coinId);

    ResponseBean update(ProjectCoinDto projectCoinDto);

    ResponseBean list(PageParamDto pageParamDto);

    ResponseBean getProjectCoins();

    ResponseBean getNonsymbolCoin();
}
