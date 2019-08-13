package com.slabs.exchange.mapper;

import com.slabs.exchange.model.entity.UserRole;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserRoleMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(UserRole record);

    int insertSelective(UserRole record);

    UserRole selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserRole record);

    int updateByPrimaryKey(UserRole record);

    void batchInsert(List<UserRole> userRoleList);

    List<UserRole> selectByUserId(Integer userId);

    void deleteByUserId(Integer id);
}