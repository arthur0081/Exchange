package com.slabs.exchange.service.back.impl;

import com.slabs.exchange.common.exception.ExchangeException;
import com.slabs.exchange.mapper.back.CoinMapper;
import com.slabs.exchange.mapper.back.ProjectCoinMapper;
import com.slabs.exchange.mapper.back.ProjectMapper;
import com.slabs.exchange.mapper.back.SymbolMapper;
import com.slabs.exchange.mapper.ext.back.SymbolExtMapper;
import com.slabs.exchange.model.common.ResponseBean;
import com.slabs.exchange.model.dto.PageParamDto;
import com.slabs.exchange.model.dto.SymbolDto;
import com.slabs.exchange.model.entity.Coin;
import com.slabs.exchange.model.entity.Project;
import com.slabs.exchange.model.entity.Symbol;
import com.slabs.exchange.service.BaseService;
import com.slabs.exchange.service.back.ISymbolService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
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
    @Resource
    private SymbolExtMapper symbolExtMapper;

    @Override
    public ResponseBean insert(SymbolDto symbolDto) {
        Symbol symbol = map(symbolDto, Symbol.class);
        // 构建币对名称
        String name = symbolDto.getCommodityName().toLowerCase() + "_" + symbolDto.getCurrencyName();
        symbol.setName(name);
        symbol.setValid(false);
        symbol.setInitPrice(new BigDecimal(0));
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
        // 得到usdt和hos
        List<Coin> coins = coinMapper.getCoins();
        // 得到项目币
        List<Coin> projectCoins = coinMapper.getNonsymbolCoin();

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
        String name = symbolDto.getCommodityName().toLowerCase() + "_" + symbolDto.getCurrencyName();
        symbol.setName(name);
        // 更新
        symbolMapper.updateByPrimaryKeySelective(symbol);

        return new ResponseBean(200, "", null);
    }

    /**
     * 币对列表
     */
    @Override
    public ResponseBean list(PageParamDto pageParamDto) {
        int total = symbolMapper.count();

        // 根据页面传递参数获取projectCoins
        int start = (pageParamDto.getCurrentPage() - 1) * pageParamDto.getPageSize();
        pageParamDto.setStart(start);
        List<Symbol> symbols = symbolMapper.list(pageParamDto);

        Map<String, Object> data = new HashMap<>();
        data.put("total", total);
        data.put("currentPage", pageParamDto.getCurrentPage());
        data.put("pageSize", pageParamDto.getPageSize());
        data.put("list", symbols);
        return new ResponseBean(200, "", data);
    }

    /**
     * 得到所有有效的币对
     */
    @Override
    public ResponseBean getAllSymbols() {
        List<Symbol> symbols = symbolMapper.getAllSymbols();

        return new ResponseBean(200, "", symbols);
    }


    /**
     * 获取所有稳定区币对
     */
    @Override
    public ResponseBean getAllStableSymbols(Integer symbolId) {
        List<Symbol> symbols = symbolMapper.getAllStableSymbols(symbolId);
        return new ResponseBean(200, "", symbols);
    }

    /**
     * 获取所有创新区币对
     */
    @Override
    public ResponseBean getAllCreationSymbols(int symbolId) {
        List<Symbol> symbols = symbolMapper.getAllCreationSymbols(symbolId);
        return new ResponseBean(200, "", symbols);
    }
}
