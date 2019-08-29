package com.slabs.exchange.mapper.back;


import com.slabs.exchange.model.dto.OrderListDto;
import com.slabs.exchange.model.dto.PageParamDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OrderMapper {
    List<OrderListDto> list(PageParamDto pageParamDto);

    int count(PageParamDto pageParamDto);
}
