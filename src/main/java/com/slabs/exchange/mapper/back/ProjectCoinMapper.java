package com.slabs.exchange.mapper.back;

import com.slabs.exchange.model.dto.PageParamDto;
import com.slabs.exchange.model.entity.ProjectCoin;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProjectCoinMapper {

    int deleteByPrimaryKey(Long id);

    int insert(ProjectCoin record);

    int insertSelective(ProjectCoin record);

    ProjectCoin selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ProjectCoin record);

    int updateByPrimaryKey(ProjectCoin record);

    int count(PageParamDto pageParamDto);

    List<ProjectCoin> list(PageParamDto pageParamDto);

    List<ProjectCoin> getProjectCoins();
}