package com.slabs.exchange.mapper.ext.back;


import com.slabs.exchange.model.dto.CoinSumDto;
import com.slabs.exchange.model.dto.SymbolDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SymbolExtMapper {
    List<SymbolDto> getSymbolIdByCoin(List<CoinSumDto> coinSumDtos);
}
