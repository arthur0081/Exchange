package com.slabs.exchange.mapper.fore;

import com.slabs.exchange.model.entity.AwardPlatformCoin;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AwardPlatformCoinMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(AwardPlatformCoin record);

    int insertSelective(AwardPlatformCoin record);

    AwardPlatformCoin selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AwardPlatformCoin record);

    int updateByPrimaryKey(AwardPlatformCoin record);
}