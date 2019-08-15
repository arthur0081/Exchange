package com.slabs.exchange.service.back.impl;

import com.slabs.exchange.mapper.ext.back.TradeExtMapper;
import com.slabs.exchange.model.common.ResponseBean;
import com.slabs.exchange.model.dto.PageParamDto;
import com.slabs.exchange.model.dto.TradeDto;
import com.slabs.exchange.model.dto.UserListDto;
import com.slabs.exchange.service.BaseService;
import com.slabs.exchange.service.back.ITradeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TradeServiceImpl extends BaseService implements ITradeService {

    @Resource
    private TradeExtMapper tradeExtMapper;

    /**
     * 成交记录的列表查询
     */
    @Override
    public ResponseBean list(PageParamDto pageParamDto) {
        int total = tradeExtMapper.count(pageParamDto);

        // 获取列表
        int start = (pageParamDto.getCurrentPage() - 1) * pageParamDto.getPageSize();
        pageParamDto.setStart(start);
        List<TradeDto> list = tradeExtMapper.list(pageParamDto);

        Map<String, Object> data = new HashMap<>();
        data.put("total", total);
        data.put("pageSize", pageParamDto.getPageSize());
        data.put("currentPage", pageParamDto.getCurrentPage());
        data.put("list", list);

        return new ResponseBean(200, "", data);
    }

}
