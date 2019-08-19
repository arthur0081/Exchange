package com.slabs.exchange.service.back.impl;

import com.slabs.exchange.common.enums.CoinEnum;
import com.slabs.exchange.mapper.UserMapper;
import com.slabs.exchange.mapper.back.ProjectCoinMapper;
import com.slabs.exchange.mapper.back.SymbolMapper;
import com.slabs.exchange.mapper.back.TradeMapper;
import com.slabs.exchange.mapper.ext.UserFundExtMapper;
import com.slabs.exchange.mapper.ext.back.TradeExtMapper;
import com.slabs.exchange.mapper.fore.UserFundMapper;
import com.slabs.exchange.model.common.ResponseBean;
import com.slabs.exchange.model.dto.*;
import com.slabs.exchange.model.entity.*;
import com.slabs.exchange.service.BaseService;
import com.slabs.exchange.service.back.IBackIndexService;
import com.slabs.exchange.util.RedisUtil;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class BackIndexServiceImpl extends BaseService implements IBackIndexService {
    private final static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    @Resource
    private RedisUtil redisUtil;
    @Resource
    private UserMapper userMapper;
    @Resource
    private TradeMapper tradeMapper;
    @Resource
    private SymbolMapper symbolMapper;
    @Resource
    private UserFundMapper userFundMapper;
    @Resource
    private ProjectCoinMapper projectCoinMapper;
    @Resource
    private UserFundExtMapper userFundExtMapper;
    @Resource
    private TradeExtMapper tradeExtMapper;


    /**
     * 后台首页展示信息逻辑
     */
    @Override
    public ResponseBean getBackIndexInfo() {
        //1.  注册用户数
        int allUserCount = userMapper.selectCount();
        //2.  活跃用户数
        int activeUserCount = 0;
        try {
            activeUserCount = redisUtil.getActiveUserCount();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //3.  昨日新增用户数
        Date lastDay = DateUtils.addDays(new Date(), -1);//得到昨天的日期
        String lastDayStr = sdf.format(lastDay);
        int lastDayRegistryCount = userMapper.selectLastDayRegistry(lastDayStr);

        //4.  平台币涨幅（如果没有的话就是0）
        //平台比的币对(内置的数据一定不会为空的)
        String symbolName = CoinEnum.HOS.getKey() + "_" + CoinEnum.USDT.getKey();
        Symbol symbol = symbolMapper.getHosByName(symbolName);
        Trade trade = tradeMapper.getLatestHosTrade(symbol.getId());
        String rateIncrease;
        String marketValue;
        String latestPrice;
        int hosAmount = userFundMapper.selectHosAmount();

        // 没有平台币的交易信息
        if(trade == null) {
            rateIncrease = "0";
            marketValue = symbol.getInitPrice().multiply(new BigDecimal(hosAmount)).setScale(6, BigDecimal.ROUND_HALF_DOWN).toString();
            latestPrice = symbol.getInitPrice().toString();
        } else {// 有平台币的交易信息
            latestPrice = trade.getPrice().toString();
            marketValue = trade.getPrice().multiply(new BigDecimal(hosAmount)).setScale(6, BigDecimal.ROUND_HALF_DOWN).toString();
            if (trade.getPrice().compareTo(symbol.getInitPrice()) < 0) {
                // 跌
                rateIncrease = "-" + symbol.getInitPrice().subtract(trade.getPrice()).divide(symbol.getInitPrice()).setScale(6, BigDecimal.ROUND_HALF_DOWN).toString();
            } else if (trade.getPrice().compareTo(symbol.getInitPrice()) == 0) {
                // 保持初始价格
                rateIncrease = "0";
            } else {
                // 涨
                rateIncrease = "+" + trade.getPrice().subtract(symbol.getInitPrice()).divide(symbol.getInitPrice()).setScale(6, BigDecimal.ROUND_HALF_DOWN).toString();
            }
        }

        PageParamDto pageParamDto = new PageParamDto();
        pageParamDto.setCoin("hos");//默认是平台币的列表
        pageParamDto.setHourChange(1);//分类：24小时换手率
        //7.  24小时换手率（降序排列）
        //8.  持币用户数（降序排列）

        //9.  持币列表  （每个用户持币数量占总数的一个百分比）
        List<HoldCoinUserDto> holdDtos = new ArrayList<>();
        int holdTotal = userFundExtMapper.count(pageParamDto);
        //根据总量进行排序
        getHoldCoinUserDtos(pageParamDto, holdDtos);

        Map<String, Object> data = new HashMap<>();
        data.put("allUserCount", allUserCount);
        data.put("activeUserCount", activeUserCount);
        data.put("lastDayRegistryCount", lastDayRegistryCount);
        data.put("rateIncrease", rateIncrease);
        data.put("marketValue", marketValue);
        data.put("latestPrice", latestPrice);

        //持币
        data.put("holdTotal", holdTotal);
        data.put("holdDtos", holdDtos);
        data.put("currentPage", pageParamDto.getCurrentPage());
        data.put("pageSize", pageParamDto.getPageSize());


        // 24小时换手率
        Map<String,Object> exchangeRateMap = getExchangeRateList(pageParamDto);
        data.put("exchangeRateList", exchangeRateMap);

        return new ResponseBean(200, "", data);
    }


    /**
     * 持币列表
     */
    @Override
    public ResponseBean getHoldCoinList(PageParamDto pageParamDto) {
        List<HoldCoinUserDto> dtos = new ArrayList<>();
        int total = userFundExtMapper.count(pageParamDto);
        //根据总量进行排序
        getHoldCoinUserDtos(pageParamDto, dtos);

        Map<String, Object> data = new HashMap<>();
        data.put("total", total);
        data.put("currentPage", pageParamDto.getCurrentPage());
        data.put("pageSize", pageParamDto.getPageSize());
        data.put("list", dtos);

        return new ResponseBean(200, "", data);
    }



    /**
     * 得到 holdCoinUserDtos
     */
    private void getHoldCoinUserDtos(PageParamDto pageParamDto, List<HoldCoinUserDto> dtos) {
        int start = (pageParamDto.getCurrentPage() - 1) * pageParamDto.getPageSize();
        pageParamDto.setStart(start);
        List<HoldCoinUserExtDto> list = userFundExtMapper.selectByCoin(pageParamDto);

        int specialAmount = 0;
        ProjectCoin projectCoin = null;
        String coin = pageParamDto.getCoin();
        //hos 或者 usdt
        if (pageParamDto.getCoin().equals(CoinEnum.HOS.getKey())
         || pageParamDto.getCoin().equals(CoinEnum.USDT.getKey())) {
            // 去t_user_fund表中拿到总和
            specialAmount = userFundMapper.selectSumByCoin(coin);
        } else {
            // 项目币
            projectCoin = projectCoinMapper.selectByCoin(coin);
        }

        for (HoldCoinUserExtDto hcued: list) {
            HoldCoinUserDto dto = new HoldCoinUserDto();
            String rate;
            //计算比率
            if(hcued.getCoin().equals(CoinEnum.HOS.getKey()) || hcued.getCoin().equals(CoinEnum.USDT.getKey())) {
                rate = hcued.getAmount().divide(new BigDecimal(specialAmount)).setScale(6, BigDecimal.ROUND_HALF_DOWN).toString();
            } else {
                rate = hcued.getAmount().divide(projectCoin.getAmount()).setScale(6, BigDecimal.ROUND_HALF_DOWN).toString();
            }
            dto.setAmount(hcued.getAmount());
            dto.setRate(rate);
            dto.setAccount(hcued.getAccount());
            dto.setWalletAddr(hcued.getWalletAddr());

            dtos.add(dto);
        }
    }


    /**
     * 项目（币种）情况概览
     */
    @Override
    public ResponseBean getCoinConditionList(PageParamDto pageParamDto) {
        Map<String, Object> data = getExchangeRateList(pageParamDto);
        return new ResponseBean(200, "", data);
    }

    /**
     *  得到24交换率列表（默认是持币用户数）
     */
    private Map<String, Object> getExchangeRateList(PageParamDto pageParamDto) {
        Map<String, Object> data = new HashMap<>();
        //a. 用户资金表 以币种去重 求得记录数即可
        int total = userFundMapper.count();
        if (pageParamDto.getHourChange().equals(1)) {//1. 24H换手率  降序排序
            //a. 用户资金表 查询币的总数并分页
            int start = (pageParamDto.getCurrentPage() - 1) * pageParamDto.getPageSize();
            pageParamDto.setStart(start);
            List<CoinSumDto> coinSumDtos = userFundMapper.selectCoinSum(pageParamDto);

            List<Symbol> symbolIds = symbolMapper.getSymbolIdByCoin(coinSumDtos);
            //c. 交易表查询24内交易的总数
            List<TradeDto> sumDtos = tradeExtMapper.getHourExchangeAmount(symbolIds);

            //d.计算换手率
            List<HoldCoinUserExchangeDto> exchangeRateList = new ArrayList<>();
            for (CoinSumDto coinSumDto: coinSumDtos) {
                String coin = coinSumDto.getCoin();
                BigDecimal allAmount = coinSumDto.getSums();
                for (Symbol symbol: symbolIds) {
                    if (coin.equals(symbol.getName())) {
                        for (TradeDto tradeDto: sumDtos) {
                            if (symbol.getId().equals(tradeDto.getSymbolId())) {
                                BigDecimal exchangeRate = tradeDto.getSums().divide(allAmount).setScale(6, BigDecimal.ROUND_HALF_DOWN);
                                HoldCoinUserExchangeDto dto = new HoldCoinUserExchangeDto();
                                dto.setCoin(coin);
                                dto.setHourExchangeRate(exchangeRate);
                                exchangeRateList.add(dto);
                            }
                        }
                    }
                }
            }

            //e. 排序换手率  (降序排列)
            if (exchangeRateList.size() > 1) {
                Collections.sort(exchangeRateList, new Comparator<HoldCoinUserExchangeDto>() {
                    /**
                     * 按照降序排列
                     */
                    @Override
                    public int compare(HoldCoinUserExchangeDto o1, HoldCoinUserExchangeDto o2) {
                        if (o1.getHourExchangeRate().compareTo(o2.getHourExchangeRate()) > 0 ) {
                            return -1;
                        }
                        if (o1.getHourExchangeRate().compareTo(o2.getHourExchangeRate()) == 0 ) {
                            return 0;
                        }
                        return 1;
                    }
                });
            }
            // 交换率列表
            data.put("exchangeRateList", exchangeRateList);

        } else {//2. 持币用户数  降序排列
            //a. 币种 和 总数， 币种分组求得记录数，每一类只取一条，并分页
            int start = (pageParamDto.getCurrentPage() - 1) * pageParamDto.getPageSize();
            pageParamDto.setStart(start);
            List<CoinUserDto> coinUserDtos = userFundMapper.selectCoinUserList(pageParamDto);
            data.put("coinUserDtos", coinUserDtos);
        }

        data.put("total", total);
        data.put("pageSize", pageParamDto.getPageSize());
        data.put("currentPage", pageParamDto.getCurrentPage());
        return data;
    }

}
