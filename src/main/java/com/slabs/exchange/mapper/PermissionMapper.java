package com.slabs.exchange.mapper;

import com.slabs.exchange.model.dto.PermissionDto;
import com.slabs.exchange.model.dto.RoleDto;
import com.slabs.exchange.model.entity.Permission;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PermissionMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(Permission record);

    int insertSelective(Permission record);

    Permission selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Permission record);

    int updateByPrimaryKey(Permission record);

    List<PermissionDto> findPermissionByRole(RoleDto roleDto);

    List<Permission> selectAll();
}