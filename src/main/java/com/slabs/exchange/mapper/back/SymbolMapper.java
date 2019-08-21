package com.slabs.exchange.mapper.back;

import com.slabs.exchange.model.dto.CoinSumDto;
import com.slabs.exchange.model.dto.PageParamDto;
import com.slabs.exchange.model.entity.Symbol;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SymbolMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(Symbol record);

    int insertSelective(Symbol record);

    Symbol selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Symbol record);

    int updateByPrimaryKey(Symbol record);

    List<Symbol> getAllValid();

    Symbol getHosByName(String symbolName);

    List<Symbol> getSymbolIdByCoin(List<CoinSumDto> coinSumDtos);

    Symbol selectByCommodity(Integer coinId);

    int count();

    List<Symbol> list(PageParamDto pageParamDto);


    List<Symbol> getAllSymbols();
}