package com.slabs.exchange.mapper.fore;

import com.slabs.exchange.model.dto.CoinSumDto;
import com.slabs.exchange.model.dto.CoinUserDto;
import com.slabs.exchange.model.dto.PageParamDto;
import com.slabs.exchange.model.entity.UserFund;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

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

    int selectHosAmount();

    int selectSumByCoin(String coin);

    int count();

    @Select("select coin, count(coin) number from t_user_fund group by coin order by number limit #{pageParamDto.pageSize} offset #{pageParamDto.start}")
    List<CoinUserDto> selectCoinUserList(PageParamDto pageParamDto);

    @Select("select coin, sum(amount) sums from t_user_fund group by coin order by sums limit #{pageParamDto.pageSize} offset #{pageParamDto.start}")
    List<CoinSumDto> selectCoinSum(PageParamDto pageParamDto);
}