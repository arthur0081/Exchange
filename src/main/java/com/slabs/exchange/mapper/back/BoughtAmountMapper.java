package com.slabs.exchange.mapper.back;

import com.slabs.exchange.model.entity.BoughtAmount;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BoughtAmountMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(BoughtAmount record);

    int insertSelective(BoughtAmount record);

    BoughtAmount selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(BoughtAmount record);

    int updateByPrimaryKey(BoughtAmount record);

    void updateWithdrawByProjectId(Integer projectId, Integer status);

    List<BoughtAmount> getWithdrawsByNum(String num, Integer status);

    void updateWithdrawStatusByOrderId(List<String> succeedList);
}