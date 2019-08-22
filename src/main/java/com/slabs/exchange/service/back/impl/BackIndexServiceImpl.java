package com.slabs.exchange.service.back.impl;

import com.slabs.exchange.common.enums.CoinEnum;
import com.slabs.exchange.mapper.UserMapper;
import com.slabs.exchange.mapper.back.ProjectCoinMapper;
import com.slabs.exchange.mapper.back.SymbolMapper;
import com.slabs.exchange.mapper.back.TradeMapper;
import com.slabs.exchange.mapper.ext.UserFundExtMapper;
import com.slabs.exchange.mapper.ext.back.ProjectCoinExtMapper;
import com.slabs.exchange.mapper.ext.back.SymbolExtMapper;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
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
    @Resource
    private ProjectCoinExtMapper projectCoinExtMapper;
    @Resource
    private SymbolExtMapper symbolExtMapper;


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
        Date date = null;
        try {
            date =  sdf.parse(lastDayStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        int lastDayRegistryCount = userMapper.selectLastDayRegistry(date);

        //4.  平台币涨幅（如果没有的话就是0）
        //平台比的币对(内置的数据一定不会为空的)
//        String symbolName = CoinEnum.HOS.getKey() + "_" + CoinEnum.USDT.getKey();
//        Symbol symbol = symbolMapper.getHosByName(symbolName);
//        Trade trade = tradeMapper.getLatestHosTrade(symbol.getId());
//        String rateIncrease;
//        String marketValue;
//        String latestPrice;
//        BigDecimal hosAmount = userFundMapper.selectHosAmount();
//
//        // 没有平台币的交易信息
//        if(trade == null) {
//            rateIncrease = "0";
//            marketValue = symbol.getInitPrice().multiply(hosAmount).setScale(6, BigDecimal.ROUND_HALF_DOWN).toString();
//            latestPrice = symbol.getInitPrice().toString();
//        } else {// 有平台币的交易信息
//            latestPrice = trade.getPrice().toString();
//            marketValue = trade.getPrice().multiply(hosAmount).setScale(6, BigDecimal.ROUND_HALF_DOWN).toString();
//            if (trade.getPrice().compareTo(symbol.getInitPrice()) < 0) {
//                // 跌
//                rateIncrease = "-" + symbol.getInitPrice().subtract(trade.getPrice()).divide(symbol.getInitPrice(), 6, BigDecimal.ROUND_HALF_DOWN).toString();
//            } else if (trade.getPrice().compareTo(symbol.getInitPrice()) == 0) {
//                // 保持初始价格
//                rateIncrease = "0";
//            } else {
//                // 涨
//                rateIncrease = "+" + trade.getPrice().subtract(symbol.getInitPrice()).divide(symbol.getInitPrice(), 6, BigDecimal.ROUND_HALF_DOWN).toString();
//            }
//        }
        Map<String, Object> data = new HashMap<>();
        data.put("allUserCount", allUserCount);
        data.put("activeUserCount", activeUserCount);
        data.put("lastDayRegistryCount", lastDayRegistryCount);

//        data.put("rateIncrease", rateIncrease);
//        data.put("marketValue", marketValue);
//        data.put("latestPrice", latestPrice);

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

        BigDecimal specialAmount = new BigDecimal(0);
        CoinDto projectCoin = null;
        String coin = pageParamDto.getCoin();
        //usdt
        if (pageParamDto.getCoin().equals(CoinEnum.USDT.getKey())) {
            // 去t_user_fund表中拿到总和
            specialAmount = userFundMapper.selectSumByCoin(coin);
        } else {
            // 项目币(或者 hos有总量))
            projectCoin = projectCoinExtMapper.selectByCoin(coin);
        }

        for (HoldCoinUserExtDto hcued: list) {
            HoldCoinUserDto dto = new HoldCoinUserDto();
            String rate;
            //计算比率
            if(hcued.getCoin().equals(CoinEnum.HOS.getKey()) || hcued.getCoin().equals(CoinEnum.USDT.getKey())) {
                rate = hcued.getAmount().divide(specialAmount, 6, BigDecimal.ROUND_HALF_DOWN).toString();
            } else {
                rate = hcued.getAmount().divide(projectCoin.getAmount(), 6, BigDecimal.ROUND_HALF_DOWN).toString();
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

        if (pageParamDto.getHourChange().equals(2)) {//2. 24H换手率  降序排序
            //a. 用户资金表 查询币的总数并分页
            int start = (pageParamDto.getCurrentPage() - 1) * pageParamDto.getPageSize();
            pageParamDto.setStart(start);
            List<CoinSumDto> coinSumDtos = userFundMapper.selectCoinSum(pageParamDto);

            List<SymbolDto> symbolIds = symbolExtMapper.getSymbolIdByCoin(coinSumDtos);
            //c. 交易表查询24内交易的总数
            List<TradeDto> sumDtos = tradeExtMapper.getHourExchangeAmount(symbolIds);

            //d.计算换手率
            List<HoldCoinUserExchangeDto> exchangeRateList = new ArrayList<>();
            for (CoinSumDto coinSumDto: coinSumDtos) {
                String coin = coinSumDto.getCoin();
                BigDecimal allAmount = coinSumDto.getSums();
                HoldCoinUserExchangeDto dto = new HoldCoinUserExchangeDto();
                dto.setCoin(coin);
                dto.setHourExchangeRate(new BigDecimal(0));//如果24小时内没有交易就是0
                for (SymbolDto symbol: symbolIds) {
                    if (coin.equals(symbol.getName())) {
                        for (TradeDto tradeDto: sumDtos) {
                            if (symbol.getId().equals(tradeDto.getSymbolId())) {
                                BigDecimal exchangeRate = tradeDto.getSums().divide(allAmount, 6, BigDecimal.ROUND_HALF_DOWN);
                                dto.setHourExchangeRate(exchangeRate);
                            }
                        }
                    }
                }
                exchangeRateList.add(dto);
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
            data.put("list", exchangeRateList);

        } else {//1. 持币用户数  降序排列
            //a. 币种 和 总数， 币种分组求得记录数，每一类只取一条，并分页
            int start = (pageParamDto.getCurrentPage() - 1) * pageParamDto.getPageSize();
            pageParamDto.setStart(start);
            List<CoinUserDto> coinUserDtos = userFundMapper.selectCoinUserList(pageParamDto);
            data.put("list", coinUserDtos);
        }

        data.put("total", total);
        data.put("pageSize", pageParamDto.getPageSize());
        data.put("currentPage", pageParamDto.getCurrentPage());
        return data;
    }

}
