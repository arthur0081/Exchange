package com.slabs.exchange.mapper.back;

import com.slabs.exchange.model.entity.BoughtAmount;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BoughtAmountMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(BoughtAmount record);

    int insertSelective(BoughtAmount record);

    BoughtAmount selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(BoughtAmount record);

    int updateByPrimaryKey(BoughtAmount record);
}