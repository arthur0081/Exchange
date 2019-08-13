package com.slabs.exchange.service.back.impl;

import com.slabs.exchange.common.exception.ExchangeException;
import com.slabs.exchange.mapper.back.CoinMapper;
import com.slabs.exchange.mapper.back.ProjectCoinMapper;
import com.slabs.exchange.mapper.back.ProjectMapper;
import com.slabs.exchange.mapper.back.SymbolMapper;
import com.slabs.exchange.model.common.ResponseBean;
import com.slabs.exchange.model.dto.SymbolDto;
import com.slabs.exchange.model.entity.Coin;
import com.slabs.exchange.model.entity.Project;
import com.slabs.exchange.model.entity.ProjectCoin;
import com.slabs.exchange.model.entity.Symbol;
import com.slabs.exchange.service.BaseService;
import com.slabs.exchange.service.back.ISymbolService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SymbolServiceImpl  extends BaseService implements ISymbolService {
    @Resource
    private SymbolMapper symbolMapper;
    @Resource
    private ProjectMapper projectMapper;
    @Resource
    private CoinMapper coinMapper;
    @Resource
    private ProjectCoinMapper projectCoinMapper;

    @Override
    public ResponseBean insert(SymbolDto symbolDto) {
        Symbol symbol = map(symbolDto, Symbol.class);
        // 构建币对名称
        String name = symbol.getCommodity() + "/" + symbol.getCurrency();
        symbol.setName(name);
        // 插入
        symbolMapper.insert(symbol);

        return new ResponseBean(200, "", null);
    }

    /**
     * 预修改
     */
    @Override
    public ResponseBean preUpdate(Integer symbolId) {
        // 币对是否被项目引用
        Project project = projectMapper.selectBySymbolId(symbolId);
        if (project != null) {
            throw new ExchangeException("不能修改，币对已经被项目使用！");
        }
        // 得到symbol
       Symbol symbol =  symbolMapper.selectByPrimaryKey(symbolId);
        // 得到USDT和HOS
        List<Coin> coins = coinMapper.getCoins();
        // 得到项目币
        List<ProjectCoin> projectCoins = projectCoinMapper.getProjectCoins();

        Map<String, Object> res = new HashMap<>();
        res.put("symbol", symbol);
        res.put("coins", coins);
        res.put("projectCoins", projectCoins);
        return new ResponseBean(200, "", res);
    }

    /**
     * 更新
     */
    @Override
    public ResponseBean update(SymbolDto symbolDto) {
        Symbol symbol =  map(symbolDto, Symbol.class);
        // 构建币对名称
        String name = symbol.getCommodity() + "/" + symbol.getCurrency();
        symbol.setName(name);
        // 更新
        symbolMapper.updateByPrimaryKey(symbol);

        return new ResponseBean(200, "", null);
    }

}
