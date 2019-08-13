package com.slabs.exchange.mapper;

import com.slabs.exchange.model.dto.RoleDto;
import com.slabs.exchange.model.dto.UserDto;
import com.slabs.exchange.model.entity.Role;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RoleMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(Role record);

    int insertSelective(Role record);

    Role selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Role record);

    int updateByPrimaryKey(Role record);

    List<RoleDto> findRoleByUser(UserDto userDto);

    List<Role> getRoles();
}