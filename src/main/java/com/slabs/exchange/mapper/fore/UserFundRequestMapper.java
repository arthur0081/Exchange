package com.slabs.exchange.mapper.fore;

import com.slabs.exchange.model.dto.PageParamDto;
import com.slabs.exchange.model.entity.UserFundRequest;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserFundRequestMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(UserFundRequest record);

    int insertSelective(UserFundRequest record);

    UserFundRequest selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserFundRequest record);

    int updateByPrimaryKey(UserFundRequest record);

    int count(Integer creditor);

    List<UserFundRequest> list(PageParamDto pageParamDto);
}