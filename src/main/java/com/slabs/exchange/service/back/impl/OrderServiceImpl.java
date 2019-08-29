package com.slabs.exchange.service.back.impl;


import com.slabs.exchange.mapper.back.OrderMapper;
import com.slabs.exchange.model.common.ResponseBean;
import com.slabs.exchange.model.dto.OrderListDto;
import com.slabs.exchange.model.dto.PageParamDto;
import com.slabs.exchange.service.BaseService;
import com.slabs.exchange.service.back.IOrderService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrderServiceImpl extends BaseService implements IOrderService {
    @Resource
    private OrderMapper orderMapper;

    /**
     * 列表查询
     */
    @Override
    public ResponseBean list(PageParamDto pageParamDto) {
        int total = orderMapper.count(pageParamDto);

        // 获取列表
        int start = (pageParamDto.getCurrentPage() - 1) * pageParamDto.getPageSize();
        pageParamDto.setStart(start);
        List<OrderListDto> list = orderMapper.list(pageParamDto);

        Map<String, Object> data = new HashMap<>();
        data.put("total", total);
        data.put("pageSize", pageParamDto.getPageSize());
        data.put("currentPage", pageParamDto.getCurrentPage());
        data.put("list", list);

        return new ResponseBean(200, "", data);
    }
}
