package com.slabs.exchange.mapper.back;

import com.slabs.exchange.model.entity.Trade;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TradeMapper {

    int deleteByPrimaryKey(Long id);

    int insert(Trade record);

    int insertSelective(Trade record);

    Trade selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Trade record);

    int updateByPrimaryKey(Trade record);

    List<Trade> getLatestTrade(List<Integer> symbolIds);

    Trade getLatestHosTrade(Integer id);
}