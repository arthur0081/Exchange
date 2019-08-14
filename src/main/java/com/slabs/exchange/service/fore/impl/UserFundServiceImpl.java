package com.slabs.exchange.service.fore.impl;

import com.slabs.exchange.common.enums.CoinEnum;
import com.slabs.exchange.mapper.back.SymbolMapper;
import com.slabs.exchange.mapper.back.TradeMapper;
import com.slabs.exchange.mapper.fore.UserFundMapper;
import com.slabs.exchange.model.common.ResponseBean;
import com.slabs.exchange.model.dto.ForeUserFundDto;
import com.slabs.exchange.model.entity.Symbol;
import com.slabs.exchange.model.entity.Trade;
import com.slabs.exchange.model.entity.UserFund;
import com.slabs.exchange.service.BaseService;
import com.slabs.exchange.service.fore.IUserFundService;
import com.slabs.exchange.util.ShiroUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserFundServiceImpl extends BaseService implements IUserFundService {

    @Resource
    private UserFundMapper userFundMapper;
    @Resource
    private SymbolMapper symbolMapper;
    @Resource
    private TradeMapper tradeMapper;

    /**
     *  各种币跟USDT的换算方式最新交易。求和。
     *  hos_usdt一开始就构建好关系(币对)，并且设定初始价格，并且永久有效。
     *  当用户进行币币交易后，交易的价格就会浮动，找到最新那个浮动即可。
     */
    @Override
    public ResponseBean list() {
        // 根据用户id得到所有的资金列表
        Integer userId = ShiroUtils.getUserId();
        List<UserFund> userFunds = userFundMapper.selectByUserId(userId);

        // 只有有效的币对，对比换算才有意义。
        List<Symbol> symbols = symbolMapper.getAllValid();

        // 用户所持有的币种关联币对，根据币对找到最新交易，通过交易计算单价，
        // 由单价格乘以用户的总额（本质上是数量），或者冻结额度（本质上也是数量）。
        // 对交易表，通过币对名称（建立索引）分组排序取到最新的一条。

        List<Integer> symbolIds = new ArrayList<>();
        Map<Integer, String> matchMap = new HashMap<>();
        for (Symbol symbol: symbols) {
            String coinName = symbol.getName().split("_")[0];// 人为控制不会出现 usdt_xxxx 情况。
            for (UserFund userFund: userFunds) {
                if (coinName.equals(userFund.getCoin())) {
                    symbolIds.add(symbol.getId());
                    matchMap.put(symbol.getId(), userFund.getCoin());
                }
            }
        }

        // 得到币种跟USDT的最新交易价格
        List<Trade> trades = tradeMapper.getLatestTrade(symbolIds);

        List<ForeUserFundDto> foreUserFundDtos = new ArrayList<>();
        BigDecimal amount = new BigDecimal(0);
        BigDecimal lock = new BigDecimal(0);
        for(Map.Entry<Integer, String> entry : matchMap.entrySet()){
            Integer symId = entry.getKey();
            String coinName = entry.getValue();
            for (Trade trade: trades) {
                if (symId.equals(trade.getSymbolId())) {
                    //计算单价
                    BigDecimal perPrice = trade.getPrice().divide(trade.getAmount());
                    for (UserFund uf: userFunds) {
                        //计算其他币转换usdt
                        calculateMoney(foreUserFundDtos, amount, lock, coinName, perPrice, uf);
                        // usdt则不用换算
                        if (CoinEnum.USDT.getKey().equals(uf.getCoin())) {
                            amount.add(uf.getAmount());
                            lock.add(uf.getOrderLocked().add(uf.getWithdrawLocked()));
                        }
                    }
                    // 如果某个具体的币没有交易的话，则使用初始价格作为换算关系
                } else {
                    //1，得到初始价格
                    for (Symbol symbol: symbols) {
                        if (symId.equals(symbol.getId())) {
                            BigDecimal initPrice = symbol.getInitPrice();
                            for (UserFund uf: userFunds) {
                                //计算其他币转换usdt
                                calculateMoney(foreUserFundDtos, amount, lock, coinName, initPrice, uf);
                            }
                        }
                    }
                }
            }
        }

        Map<String, Object> data = new HashMap<>();
        data.put("amount", amount);
        data.put("lock", lock);
        data.put("list", foreUserFundDtos);

        return new ResponseBean(200, "", data);
    }

    /**
     * 计算其他币转换usdt
     * @param foreUserFundDtos
     * @param amount
     * @param lock
     * @param coinName
     * @param perPrice
     * @param uf
     */
    private void calculateMoney(List<ForeUserFundDto> foreUserFundDtos, BigDecimal amount, BigDecimal lock, String coinName, BigDecimal perPrice, UserFund uf) {
        if (coinName.equals(uf.getCoin())) {
            BigDecimal ufAmount = uf.getAmount();
            BigDecimal ufOrderLock = uf.getOrderLocked();
            BigDecimal ufWithdrawLock = uf.getWithdrawLocked();
            // 总额
            BigDecimal usdtAmount = perPrice.multiply(ufAmount);
            amount.add(usdtAmount);
            // 冻结
            BigDecimal orderLock = perPrice.multiply(ufOrderLock);
            BigDecimal withdrawLock = perPrice.multiply(ufWithdrawLock);
            lock.add(orderLock).add(withdrawLock);
            // 构建响应dto
            ForeUserFundDto fuf = new ForeUserFundDto();
            fuf.setCoin(uf.getCoin());
            fuf.setAvailable(ufAmount.subtract(ufOrderLock.add(ufWithdrawLock)));
            fuf.setLock(ufOrderLock.add(ufWithdrawLock));
            foreUserFundDtos.add(fuf);
        }
    }


}
