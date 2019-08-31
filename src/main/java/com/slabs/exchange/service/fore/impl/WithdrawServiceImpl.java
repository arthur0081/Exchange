package com.slabs.exchange.service.fore.impl;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.slabs.exchange.common.config.ExchangeApiProperties;
import com.slabs.exchange.common.enums.CoinEnum;
import com.slabs.exchange.common.exception.ExchangeException;
import com.slabs.exchange.mapper.UserMapper;
import com.slabs.exchange.mapper.back.CoinMapper;
import com.slabs.exchange.mapper.fore.UserFundMapper;
import com.slabs.exchange.mapper.fore.WithdrawMapper;
import com.slabs.exchange.model.common.ResponseBean;
import com.slabs.exchange.model.dto.ApiWithdrawDto;
import com.slabs.exchange.model.dto.ExchangeApiResDto;
import com.slabs.exchange.model.dto.WalletAndContractAddrDto;
import com.slabs.exchange.model.dto.WithdrawDto;
import com.slabs.exchange.model.entity.User;
import com.slabs.exchange.model.entity.UserFund;
import com.slabs.exchange.model.entity.Withdraw;
import com.slabs.exchange.service.BaseService;
import com.slabs.exchange.service.fore.IWithdrawService;
import com.slabs.exchange.util.AESUtil;
import com.slabs.exchange.util.ExchangePreconditions;
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
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
@Slf4j
public class WithdrawServiceImpl extends BaseService implements IWithdrawService {
    private static final Gson gson = new GsonBuilder().create();
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    @Resource
    private UserMapper userMapper;
    @Resource
    private WithdrawMapper withdrawMapper;
    @Resource
    private ExchangeApiProperties exchangeApiProperties;
    @Resource
    private CoinMapper coinMapper;
    @Resource
    private UserFundMapper userFundMapper;

    @Override
    public ResponseBean withdraw(WithdrawDto withdrawDto) {
        ExchangePreconditions.notNull(withdrawDto, "参数不能为空！");
        //用户资金密码进行校验
        Integer userId = ShiroUtils.getUserId();
        User user = userMapper.selectByPrimaryKey(userId);
        String plainPassword = AESUtil.desEncrypt(withdrawDto.getFundPassword());
        if (!user.getFundPassword().equals(ShiroUtils.encodeSalt(plainPassword, user.getSalt()))) {
            throw new ExchangeException("输入的资金密码错误！");
        }

        // 币的合约地址
        WalletAndContractAddrDto walletAndContractAddrDto = coinMapper.getWalletAndContractAddrByCoinName(withdrawDto.getCoin());
        if (walletAndContractAddrDto == null) {
            throw new ExchangeException("交易系统不支持此币提现");
        }

        // 判断当前登陆用户是否有此币的足够余额
        UserFund userFund = userFundMapper.selectByUserIdAndCoinName(userId, withdrawDto.getCoin());
        if (userFund == null || userFund.getAmount().compareTo(withdrawDto.getAmount()) < 0) {
            throw new ExchangeException("用户的"+ withdrawDto.getCoin() + "余额不足，请充值！");
        }

        //调用交易引擎的提现接口， 它会返回id（数字的字符串类型）
        ApiWithdrawDto apiWithdrawDto = new ApiWithdrawDto();
        // 后期都修改成可配置
        apiWithdrawDto.setAmount(withdrawDto.getAmount());
        apiWithdrawDto.setCoin(withdrawDto.getCoin());
        String requestData = gson.toJson(apiWithdrawDto);
        MediaType mediaType = MediaType.parse("text/x-markdown; charset=utf-8");
        Request request = new Request.Builder()
                .url(exchangeApiProperties.getHost() + exchangeApiProperties.getWithdraw())
                .post(RequestBody.create(mediaType, requestData))
                .header("Authorization", "Bearer" + " " + JWTUtil.encode(userId.toString()))//当前登陆者
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
        withdraw.setAmount(withdrawDto.getAmount());//提的金额
        withdraw.setCoin(withdrawDto.getCoin());//提的币种
        withdraw.setStatus(false);
        //总账户钱包地址(总账户的id就是1，它可以登陆前台，也可以登陆后台)
        User adminUser = userMapper.selectByPrimaryKey(1);
        // 总账户的钱包地址是希望隐藏起来的
        withdraw.setSender(adminUser.getWalletAddr());
        // 用户填写的钱包地址（可能是自己的，也可能是别人的）
        withdraw.setReceiver(withdrawDto.getWalletAddr());
        withdraw.setTime(new Date());
        withdraw.setContractAddr(walletAndContractAddrDto.getContractAddr());
        // 交易所提现接口返回id
        withdraw.setApiResponseId(exchangeApiResDto.getId());
        // 记录当前操作人是谁（这个很重要，他会跟总账户做 加或减）
        // 充值的时候，充值用户做加，而总账户做减法；提现的时候，提现用户做减法，而总账户做加法。
        withdraw.setReceiverId(user.getId());

        withdrawMapper.insert(withdraw);

        return new ResponseBean(200, "", null);
    }
}
