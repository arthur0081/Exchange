package com.slabs.exchange.service.fore.impl;

import com.slabs.exchange.mapper.fore.UserFundRequestMapper;
import com.slabs.exchange.model.common.ResponseBean;
import com.slabs.exchange.model.dto.PageParamDto;
import com.slabs.exchange.model.entity.UserFundRequest;
import com.slabs.exchange.service.BaseService;
import com.slabs.exchange.service.fore.IUserFundRequestService;
import com.slabs.exchange.util.ShiroUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 前台：用户充值提现记录表
 */
@Service
public class UserFundRequestServiceImpl extends BaseService implements IUserFundRequestService {

    @Resource
    private UserFundRequestMapper userFundRequestMapper;

    /**
     *  只是列表查询，没有模糊查询字段
     */
    @Override
    public ResponseBean list(PageParamDto pageParamDto) {
        Integer userId = ShiroUtils.getUserId();
        int total = userFundRequestMapper.count(userId);
        int start = (pageParamDto.getCurrentPage() - 1) * pageParamDto.getPageSize();
        pageParamDto.setStart(start);
        pageParamDto.setDebitor(userId);
        List<UserFundRequest> list = userFundRequestMapper.list(pageParamDto);
        Map<String, Object> data = new HashMap<>();
        data.put("currentPage", pageParamDto.getCurrentPage());
        data.put("pageSize", pageParamDto.getPageSize());
        data.put("list", list);
        data.put("total", total);
        return new ResponseBean(200, "",data);
    }
}
