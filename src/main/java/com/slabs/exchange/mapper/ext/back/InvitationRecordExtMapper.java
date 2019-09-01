package com.slabs.exchange.mapper.ext.back;

import com.slabs.exchange.model.dto.InvitationRecordDto;
import com.slabs.exchange.model.dto.PageParamDto;
import com.sun.jmx.snmp.SnmpTooBigException;
import org.apache.ibatis.annotations.Mapper;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface InvitationRecordExtMapper {


    int count(PageParamDto pageParamDto);

    List<InvitationRecordDto> list(PageParamDto pageParamDto);

    List<String> getInviteeUsers(int userId);

    int myInvitationCount(int userId);

    List<InvitationRecordDto> myInvitationList(PageParamDto pageParamDto);

    BigDecimal myInvitationAllAmount(Integer userId);
}