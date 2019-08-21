package com.slabs.exchange.mapper.ext.back;


import com.slabs.exchange.model.dto.CoinDto;
import com.slabs.exchange.model.dto.PageParamDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProjectCoinExtMapper {

    int count();

    List<CoinDto> list(PageParamDto pageParamDto);
}
