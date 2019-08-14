package com.slabs.exchange.mapper.fore;

import com.slabs.exchange.model.entity.UserFund;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserFundMapper {

    int deleteByPrimaryKey(Long id);

    int insert(UserFund record);

    int insertSelective(UserFund record);

    UserFund selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UserFund record);

    int updateByPrimaryKey(UserFund record);

    List<UserFund> selectByUserId(Integer userId);

    UserFund selectByUserIdAndUsdt(Integer userId);
}