package com.slabs.exchange.mapper.fore;

import com.slabs.exchange.model.entity.UserFund;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserFundMapper {

    int deleteByPrimaryKey(Long id);

    int insert(UserFund record);

    int insertSelective(UserFund record);

    UserFund selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UserFund record);

    int updateByPrimaryKey(UserFund record);

    UserFund selectByUserId(Integer userId);
}