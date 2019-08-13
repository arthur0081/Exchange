package com.slabs.exchange.mapper.ext;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface PermissionExtMapper {
    @Select("SELECT DISTINCT p.name FROM permission p INNER JOIN role_permission rp ON rp.role_id = #{roleId} AND rp.permission_id = p.id")
    List<String> queryNamesByRoleId(Long roleId);

}
