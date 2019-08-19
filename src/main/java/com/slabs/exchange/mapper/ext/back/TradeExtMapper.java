package com.slabs.exchange.mapper.ext.back;


import com.slabs.exchange.model.dto.PageParamDto;
import com.slabs.exchange.model.dto.TradeDto;
import com.slabs.exchange.model.entity.Symbol;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TradeExtMapper {

    int count(PageParamDto pageParamDto);

    List<TradeDto> list(PageParamDto pageParamDto);

    List<TradeDto> getHourExchangeAmount(List<Symbol> symbolIds);
}
