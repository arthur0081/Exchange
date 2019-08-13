package com.slabs.exchange.mapper;

import com.slabs.exchange.model.entity.RolePermission;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RolePermissionMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(RolePermission record);

    int insertSelective(RolePermission record);

    RolePermission selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RolePermission record);

    int updateByPrimaryKey(RolePermission record);

    void deleteByRoleId(Integer id);

    void batchInsert(List<RolePermission> rolePermissions);

    List<RolePermission> selectByRoleId(Integer id);
}