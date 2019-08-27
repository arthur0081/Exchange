package com.slabs.exchange.mapper.fore;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface WithdrawMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(Withdraw record);

    int insertSelective(Withdraw record);

    Withdraw selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Withdraw record);

    int updateByPrimaryKey(Withdraw record);
}