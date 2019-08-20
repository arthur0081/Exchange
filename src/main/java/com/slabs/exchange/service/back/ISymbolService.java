package com.slabs.exchange.service.back;

import com.slabs.exchange.model.common.ResponseBean;
import com.slabs.exchange.model.dto.PageParamDto;
import com.slabs.exchange.model.dto.SymbolDto;

public interface ISymbolService {
    ResponseBean insert(SymbolDto symbolDto);

    ResponseBean preUpdate(Integer symbolId);

    ResponseBean update(SymbolDto symbolDto);

    ResponseBean list(PageParamDto pageParamDto);

    ResponseBean getValidSymbol();
}
