package com.slabs.exchange.mapper.ext;


import com.slabs.exchange.model.dto.HoldCoinUserExtDto;
import com.slabs.exchange.model.dto.PageParamDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserFundExtMapper {

    int count(PageParamDto pageParamDto);

    List<HoldCoinUserExtDto> selectByCoin(PageParamDto pageParamDto);
}
