package com.slabs.exchange.mapper;

import com.slabs.exchange.model.dto.AccountCheckDto;
import com.slabs.exchange.model.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;
import java.util.List;

@Mapper
public interface UserMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    User queryByAccount(String account);

    User selectByInvitationCode(String invitationCode);

    int selectCount();

    int selectLastDayRegistry(Date lastDay);

    User checkAccount(AccountCheckDto accountCheckDto);

    List<User> getProjectUsers();
}