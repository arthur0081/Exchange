package com.slabs.exchange.service.fore.impl;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.slabs.exchange.common.config.ExchangeApiProperties;
import com.slabs.exchange.common.exception.ExchangeException;
import com.slabs.exchange.mapper.UserMapper;
import com.slabs.exchange.mapper.back.CoinMapper;
import com.slabs.exchange.mapper.fore.WithdrawMapper;
import com.slabs.exchange.model.common.ResponseBean;
import com.slabs.exchange.model.dto.ApiWithdrawDto;
import com.slabs.exchange.model.dto.ExchangeApiResDto;
import com.slabs.exchange.model.dto.WalletAndContractAddrDto;
import com.slabs.exchange.model.dto.WithdrawDto;
import com.slabs.exchange.model.entity.User;
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
        // 平台币持有人的钱包地址和币的合约地址
        WalletAndContractAddrDto walletAndContractAddrDto = coinMapper.getWalletAndContractAddrByCoinName(withdrawDto.getCoin());
        //调用交易引擎的提现接口， 它会返回id（数字的字符串类型）
        ApiWithdrawDto apiWithdrawDto = new ApiWithdrawDto();
        // 后期都修改成可配置
        apiWithdrawDto.setAmount(withdrawDto.getAmount());
        apiWithdrawDto.setCoin(withdrawDto.getCoin());//hos
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
            log.error("user id:" + ShiroUtils.getUserId() + "withdraw failed." + sdf.format(new Date()));
            throw new ExchangeException("提现失败！");
        }
        ExchangeApiResDto exchangeApiResDto = gson.fromJson(resData, ExchangeApiResDto.class);
        if (exchangeApiResDto.getId() == null) {
            log.error("user id:" + ShiroUtils.getUserId() + "withdraw failed." + sdf.format(new Date()));
            throw new ExchangeException("提现失败！!" + resData);
        }

        Withdraw withdraw = new Withdraw();
        withdraw.setAmount(withdrawDto.getAmount());
        withdraw.setCoin(withdrawDto.getCoin());//hos
        withdraw.setStatus(false);
        withdraw.setSender(walletAndContractAddrDto.getWalletAddr());//该币持有人的钱包地址
        withdraw.setReceiver(user.getWalletAddr());
        withdraw.setTime(new Date());
        withdraw.setContractAddr(walletAndContractAddrDto.getContractAddr());
        // 交易所提现接口返回id
        withdraw.setApiResponseId(exchangeApiResDto.getId());
        withdraw.setReceiverId(user.getId());

        withdrawMapper.insert(withdraw);

        return new ResponseBean(200, "", null);
    }
}
