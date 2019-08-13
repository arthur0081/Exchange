package com.slabs.exchange.mapper.back;

import com.slabs.exchange.model.entity.AttachFile;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AttachFileMapper {

    int deleteByPrimaryKey(Long id);

    int insert(AttachFile record);

    int insertSelective(AttachFile record);

    AttachFile selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(AttachFile record);

    int updateByPrimaryKey(AttachFile record);

    void batchInsert(List<AttachFile> list);

    List<AttachFile> selectByTypeAndRefId(String type, Long refId);

    void deleteByTypeAndRefId(String type, Long refId);
}