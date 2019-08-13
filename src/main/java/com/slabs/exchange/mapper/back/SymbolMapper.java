package com.slabs.exchange.mapper.back;

import com.slabs.exchange.model.entity.Symbol;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SymbolMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(Symbol record);

    int insertSelective(Symbol record);

    Symbol selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Symbol record);

    int updateByPrimaryKey(Symbol record);
}