package com.slabs.exchange.mapper.ext;

import com.slabs.exchange.model.dto.PageParamDto;
import com.slabs.exchange.model.dto.UserListDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserExtMapper {
    List<UserListDto> list(PageParamDto pageParamDto);

    int count();
}