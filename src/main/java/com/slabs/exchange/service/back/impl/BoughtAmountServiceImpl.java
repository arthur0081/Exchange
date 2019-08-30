package com.slabs.exchange.service.back.impl;


import com.slabs.exchange.mapper.ext.back.BoughtAmountExtMapper;
import com.slabs.exchange.model.common.ResponseBean;
import com.slabs.exchange.model.dto.BoughtAmountDto;
import com.slabs.exchange.model.dto.PageParamDto;
import com.slabs.exchange.service.BaseService;
import com.slabs.exchange.service.back.IBoughtAmountService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BoughtAmountServiceImpl extends BaseService implements IBoughtAmountService {
    @Resource
    private BoughtAmountExtMapper boughtAmountExtMapper;

    @Override
    public ResponseBean list(PageParamDto pageParamDto) {
        int total = boughtAmountExtMapper.count(pageParamDto);

        int start = (pageParamDto.getCurrentPage() - 1) * pageParamDto.getPageSize();
        pageParamDto.setStart(start);
        List<BoughtAmountDto> list = boughtAmountExtMapper.list(pageParamDto);

        Map<String, Object> data = new HashMap<>();
        data.put("total", total);
        data.put("pageSize", pageParamDto.getPageSize());
        data.put("currentPage", pageParamDto.getCurrentPage());
        data.put("list", list);

        return new ResponseBean(200, "", data);
    }

}
