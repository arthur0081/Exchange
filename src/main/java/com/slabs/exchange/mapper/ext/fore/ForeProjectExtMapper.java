package com.slabs.exchange.mapper.ext.fore;

import com.slabs.exchange.model.dto.ForeProjectDto;
import com.slabs.exchange.model.dto.PageParamDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ForeProjectExtMapper {

    int count(PageParamDto pageParamDto);

    List<ForeProjectDto> list(PageParamDto pageParamDto);
}
