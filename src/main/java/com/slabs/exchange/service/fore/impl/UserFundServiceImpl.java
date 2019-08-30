package com.slabs.exchange.service.fore.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.slabs.exchange.common.config.ExchangeApiProperties;
import com.slabs.exchange.common.config.PlatformCoinProperties;
import com.slabs.exchange.common.enums.CoinEnum;
import com.slabs.exchange.common.exception.ExchangeException;
import com.slabs.exchange.mapper.UserMapper;
import com.slabs.exchange.mapper.back.CoinMapper;
import com.slabs.exchange.mapper.back.SymbolMapper;
import com.slabs.exchange.mapper.back.TradeMapper;
import com.slabs.exchange.mapper.fore.UserFundMapper;
import com.slabs.exchange.mapper.fore.WithdrawMapper;
import com.slabs.exchange.model.common.ResponseBean;
import com.slabs.exchange.model.dto.*;
import com.slabs.exchange.model.entity.*;
import com.slabs.exchange.service.BaseService;
import com.slabs.exchange.service.fore.IUserFundService;
import com.slabs.exchange.util.JWTUtil;
import com.slabs.exchange.util.ShiroUtils;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Slf4j
public class UserFundServiceImpl extends BaseService implements IUserFundService {
    private static final Gson gson = new GsonBuilder().create();
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    @Resource
    private UserFundMapper userFundMapper;
    @Resource
    private SymbolMapper symbolMapper;
    @Resource
    private TradeMapper tradeMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    private WithdrawMapper withdrawMapper;
    @Resource
    private CoinMapper coinMapper;
    @Resource
    private ExchangeApiProperties exchangeApiProperties;
    @Resource
    private PlatformCoinProperties platformCoinProperties;

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

        // 只有有效的币对，对币换算才有意义。
        List<Symbol> symbols = symbolMapper.getAllValid();

        // 用户所持有的币种关联币对，根据币对找到最新交易，通过交易计算单价，
        // 由单价乘以用户的总额（本质上是数量），或者冻结额度（本质上也是数量）。
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
        List<Trade> trades = null;
        if (symbolIds.size() > 0) {
            trades = tradeMapper.getLatestTrade(symbolIds);
        }
        List<ForeUserFundDto> foreUserFundDtos = new ArrayList<>();
        BigDecimal amount = new BigDecimal(0);
        BigDecimal lock = new BigDecimal(0);
        for(Map.Entry<Integer, String> entry : matchMap.entrySet()){
            Integer symId = entry.getKey();
            String coinName = entry.getValue();
            if (trades != null) {
                for (Trade trade: trades) {
                    if (symId.equals(trade.getSymbolId())) {
                        //单价
                        BigDecimal perPrice = trade.getPrice();
                        for (UserFund uf: userFunds) {
                            //计算其他币转换usdt
                            Map<String, BigDecimal> map = calculateMoney(foreUserFundDtos, amount, lock, coinName, perPrice, uf);
                            // usdt则不用换算
                            amount = map.get("amount");
                            lock = map.get("lock");
                            if (CoinEnum.USDT.getKey().equals(uf.getCoin())) {
                                amount = amount.add(uf.getAmount());
                                lock = lock.add(uf.getOrderLocked().add(uf.getWithdrawLocked()));
                            }
                        }
                        // 如果某个具体的币没有交易的话，则使用初始价格作为换算关系
                    } else {
                        //1.初始价格
                        for (Symbol symbol: symbols) {
                            if (symId.equals(symbol.getId())) {
                                BigDecimal initPrice = symbol.getInitPrice();
                                for (UserFund uf: userFunds) {
                                    //2.计算其他币转换usdt
                                    Map<String,BigDecimal> map = calculateMoney(foreUserFundDtos, amount, lock, coinName, initPrice, uf);
                                    amount = map.get("amount");
                                    lock = map.get("lock");
                                }
                            }
                        }
                    }
                }

            } else {
                //1.初始价格
                for (Symbol symbol: symbols) {
                    if (symId.equals(symbol.getId())) {
                        BigDecimal initPrice = symbol.getInitPrice();
                        for (UserFund uf: userFunds) {
                            //2.计算其他币转换usdt
                            Map<String,BigDecimal> map = calculateMoney(foreUserFundDtos, amount, lock, coinName, initPrice, uf);
                            amount = map.get("amount");
                            lock = map.get("lock");
                        }
                    }
                }
            }
        }

        Map<String, Object> data = new HashMap<>();
        data.put("amount", amount.setScale(6, BigDecimal.ROUND_HALF_DOWN));
        data.put("lock", lock.setScale(6, BigDecimal.ROUND_HALF_DOWN));
        data.put("list", foreUserFundDtos);
        User user = userMapper.selectByPrimaryKey(ShiroUtils.getUserId());
        data.put("walletAddr", user.getWalletAddr());
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
    private Map<String, BigDecimal> calculateMoney(List<ForeUserFundDto> foreUserFundDtos, BigDecimal amount, BigDecimal lock, String coinName, BigDecimal perPrice, UserFund uf) {
        Map<String, BigDecimal> map = new HashMap<>();
        if (coinName.equals(uf.getCoin())) {
            BigDecimal ufAmount = uf.getAmount();
            BigDecimal ufOrderLock = uf.getOrderLocked();
            BigDecimal ufWithdrawLock = uf.getWithdrawLocked();

            // 总额
            BigDecimal usdtAmount = perPrice.multiply(ufAmount);
            amount = amount.add(usdtAmount);

            // 冻结
            BigDecimal orderLock = perPrice.multiply(ufOrderLock);
            lock = lock.add(orderLock);
            BigDecimal withdrawLock = perPrice.multiply(ufWithdrawLock);
            lock = lock.add(withdrawLock);

            // 构建响应dto
            ForeUserFundDto fuf = new ForeUserFundDto();
            fuf.setCoin(uf.getCoin());
            BigDecimal avai = ufAmount.subtract(ufOrderLock.add(ufWithdrawLock));
            fuf.setAvailable(avai.setScale(6, BigDecimal.ROUND_HALF_DOWN));
            fuf.setLock(ufOrderLock.add(ufWithdrawLock).setScale(6, BigDecimal.ROUND_HALF_DOWN));
            // USDT估值是对可用余额的估值
            fuf.setAmount(perPrice.multiply(avai).setScale(6, BigDecimal.ROUND_HALF_DOWN));
            foreUserFundDtos.add(fuf);
            // 基本数据类型，通过方法的时候，不是引用传递，而是值传递，所以需要返回值
            // 如果是引用数据类型，则通过方法的时候，是引用传递，这个时候在同一个作用域依然有效（更改后）。
            map.put("amount", amount);
            map.put("lock", lock);
        }
        map.put("amount", amount);
        map.put("lock", lock);
        return map;
    }


    /**
     *  充值和提现，黄毅的接口（交易引擎服务）都是直接修改数据库表的。
     */
    @Override
    public ResponseBean getWalletAddr() {
        User user = userMapper.selectByPrimaryKey(ShiroUtils.getUserId());
        return new ResponseBean(200, "", user.getWalletAddr());
    }

    /**
     * 提现
     */
    @Override
    public ResponseBean withdraw(WithdrawDto withdrawDto) {
        // 该币持有人的钱包地址和该币的合约地址
        WalletAndContractAddrDto walletAndContractAddrDto = coinMapper.getWalletAndContractAddrByCoinName(platformCoinProperties.getCoinName());
        //调用交易引擎的提现接口， 它会返回id（数字的字符串类型）
        ApiWithdrawDto apiWithdrawDto = new ApiWithdrawDto();
        apiWithdrawDto.setAmount(withdrawDto.getAmount());
        apiWithdrawDto.setCoin(withdrawDto.getCoin());
        String requestData = gson.toJson(apiWithdrawDto);
        MediaType mediaType = MediaType.parse("text/x-markdown; charset=utf-8");
        Request request = new Request.Builder()
                .url(exchangeApiProperties.getHost() + exchangeApiProperties.getWithdraw())
                .post(RequestBody.create(mediaType, requestData))
                .header("Authorization", "Bearer" + " " + JWTUtil.encode(walletAndContractAddrDto.getUserId().toString()))//平台币的持有人
                .build();
        OkHttpClient okHttpClient = new OkHttpClient();
        String resData = "";
        try {
            resData = okHttpClient.newCall(request).execute().body().string();
        } catch (IOException e) {
            e.printStackTrace();
            log.error("network question: user id:" + ShiroUtils.getUserId() + "withdraw failed." + sdf.format(new Date()));
            throw new ExchangeException("提现失败！");
        }
        ExchangeApiResDto exchangeApiResDto = gson.fromJson(resData, ExchangeApiResDto.class);
        if (exchangeApiResDto.getId() == null) {
            log.error("business question: user id:" + ShiroUtils.getUserId() + "withdraw failed." + sdf.format(new Date()));
            throw new ExchangeException("提现失败！!" + resData);
        }

        Withdraw withdraw = new Withdraw();
        withdraw.setAmount(platformCoinProperties.getAwardAmount());
        withdraw.setCoin(platformCoinProperties.getCoinName());//hos
        withdraw.setStatus(false);

        withdraw.setSender(walletAndContractAddrDto.getWalletAddr());//该币持有人的钱包地址（usdt总地址或者项目币控制人的地址）
        withdraw.setReceiver(withdrawDto.getWalletAddr());
        withdraw.setTime(new Date());
        withdraw.setContractAddr(walletAndContractAddrDto.getContractAddr());
        // 交易所提现接口返回id
        withdraw.setApiResponseId(exchangeApiResDto.getId());

        withdraw.setReceiverId(ShiroUtils.getUserId());

        withdrawMapper.insert(withdraw);

        return new ResponseBean(200, "","提现成功！");

    }

}
