package com.slabs.exchange.mapper.ext.back;

import com.slabs.exchange.model.dto.BoughtAmountDto;
import com.slabs.exchange.model.dto.ForeProjectDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BoughtAmountExtMapper {

    List<BoughtAmountDto> getBoughtAmount(List<ForeProjectDto> foreProjectList);

    BoughtAmountDto getAmountByProjectId(Long projectId);
}