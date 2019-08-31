package com.slabs.exchange.mapper.fore;

import com.slabs.exchange.model.dto.BenefitInviteeUserDto;
import com.slabs.exchange.model.entity.InvitationRecord;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface InvitationRecordMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(InvitationRecord record);

    int insertSelective(InvitationRecord record);

    InvitationRecord selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(InvitationRecord record);

    int updateByPrimaryKey(InvitationRecord record);

    List<InvitationRecord> selectByAwardValid(boolean valid);

    void batchUpdateAwardValidByBenefitUser(List<BenefitInviteeUserDto> successList);
}