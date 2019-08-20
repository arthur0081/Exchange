package com.slabs.exchange.mapper;

import com.slabs.exchange.model.dto.AccountCheckDto;
import com.slabs.exchange.model.dto.UserDto;
import com.slabs.exchange.model.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;

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

    int selectLastDayRegistry(String lastDay);

    User checkAccount(AccountCheckDto accountCheckDto);
}