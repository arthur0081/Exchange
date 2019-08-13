package com.slabs.exchange.mapper.ext.back;

import com.slabs.exchange.model.dto.PageParamDto;
import com.slabs.exchange.model.dto.ProjectListDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProjectExtMapper {

    int count(PageParamDto pageParamDto);

    List<ProjectListDto> list(PageParamDto pageParamDto);
}
