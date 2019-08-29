package com.slabs.exchange.service.fore.impl;


import com.slabs.exchange.mapper.ext.back.InvitationRecordExtMapper;
import com.slabs.exchange.model.common.ResponseBean;
import com.slabs.exchange.model.dto.InvitationRecordDto;
import com.slabs.exchange.model.dto.PageParamDto;
import com.slabs.exchange.service.BaseService;
import com.slabs.exchange.service.fore.IInvitationRecordService;
import com.slabs.exchange.util.ShiroUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class InvitationRecordServiceImpl extends BaseService implements IInvitationRecordService {
    @Resource
    private InvitationRecordExtMapper invitationRecordExtMapper;

    @Override
    public ResponseBean list(PageParamDto pageParamDto) {
        int total = invitationRecordExtMapper.count(pageParamDto);
        int start = (pageParamDto.getCurrentPage() - 1) * pageParamDto.getPageSize();
        pageParamDto.setStart(start);
        List<InvitationRecordDto> list = invitationRecordExtMapper.list(pageParamDto);

        Map<String, Object> data = new HashMap<>();
        data.put("total", total);
        data.put("pageSize", pageParamDto.getPageSize());
        data.put("currentPage", pageParamDto.getCurrentPage());
        data.put("list", list);

        return new ResponseBean(200, "", data);
    }

    @Override
    public ResponseBean getDetail(int userId) {
        List<String> list = invitationRecordExtMapper.getInviteeUsers(userId);
        return new ResponseBean(200, "", list);
    }

    @Override
    public ResponseBean getMyInvitation(PageParamDto pageParamDto) {
        Integer userId = ShiroUtils.getUserId();
        int total = invitationRecordExtMapper.myInvitationCount(userId);

        int start = (pageParamDto.getCurrentPage() - 1) * pageParamDto.getPageSize();
        pageParamDto.setStart(start);
        pageParamDto.setUserId(userId);
        List<InvitationRecordDto> list = invitationRecordExtMapper.myInvitationList(pageParamDto);

        Map<String, Object> data = new HashMap<>();
        data.put("total", total);
        data.put("pageSize", pageParamDto.getPageSize());
        data.put("currentPage", pageParamDto.getCurrentPage());
        data.put("list", list);

        return new ResponseBean(200, "", data);
    }
}
