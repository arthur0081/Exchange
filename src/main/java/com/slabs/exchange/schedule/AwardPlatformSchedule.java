package com.slabs.exchange.schedule;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.slabs.exchange.common.config.ExchangeApiProperties;
import com.slabs.exchange.common.config.PlatformCoinProperties;
import com.slabs.exchange.mapper.UserMapper;
import com.slabs.exchange.mapper.back.CoinMapper;
import com.slabs.exchange.mapper.fore.InvitationRecordMapper;
import com.slabs.exchange.mapper.fore.UserFundMapper;
import com.slabs.exchange.mapper.fore.WithdrawMapper;
import com.slabs.exchange.model.dto.ApiWithdrawDto;
import com.slabs.exchange.model.dto.BenefitInviteeUserDto;
import com.slabs.exchange.model.dto.ExchangeApiResDto;
import com.slabs.exchange.model.dto.WalletAndContractAddrDto;
import com.slabs.exchange.model.entity.InvitationRecord;
import com.slabs.exchange.model.entity.User;
import com.slabs.exchange.model.entity.UserFund;
import com.slabs.exchange.model.entity.Withdraw;
import com.slabs.exchange.util.JWTUtil;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * 邀请注册的平台币奖励逻辑
 */
@Component
@Slf4j
public class AwardPlatformSchedule {
    private static final Gson gson = new GsonBuilder().create();
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    @Resource
    private InvitationRecordMapper invitationRecordMapper;
    @Resource
    private PlatformCoinProperties platformCoinProperties;
    @Resource
    private CoinMapper coinMapper;
    @Resource
    private ExchangeApiProperties exchangeApiProperties;
    @Resource
    private WithdrawMapper withdrawMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    private UserFundMapper userFundMapper;

    @Scheduled(cron = "0 0/10 * * * ?")
    public void awardPlatformCoin() {
        while (true) {
            List<InvitationRecord> list = invitationRecordMapper.selectByAwardValid(false);
            if (list.size() <= 0) {
                return;
            }
            List<BenefitInviteeUserDto> successList = new ArrayList<>();
            for (InvitationRecord invitationRecord: list) {
                List<UserFund> userFunds = userFundMapper.selectByUserId(invitationRecord.getInviteeUser());
                if (userFunds.size() <= 0) {
                    continue;
                }
                // 给主动邀请的人发放 平台币，直接调用提现接口。（提现的概念来描述从一个钱包地址(总账户钱包地址)转账到另一个钱包地址）
                // 该币的合约地址
                WalletAndContractAddrDto walletAndContractAddrDto = coinMapper.getWalletAndContractAddrByCoinName(platformCoinProperties.getCoinName());
                //调用交易引擎的提现接口， 它会返回id（数字的字符串类型）
                ApiWithdrawDto apiWithdrawDto = new ApiWithdrawDto();
                apiWithdrawDto.setAmount(platformCoinProperties.getAwardAmount());
                apiWithdrawDto.setCoin(platformCoinProperties.getCoinName());//hos
                String requestData = gson.toJson(apiWithdrawDto);
                MediaType mediaType = MediaType.parse("text/x-markdown; charset=utf-8");
                Request request = new Request.Builder()
                        .url(exchangeApiProperties.getHost() + exchangeApiProperties.getWithdraw())
                        .post(RequestBody.create(mediaType, requestData))
                        .header("Authorization", "Bearer" + " " + JWTUtil.encode("1"))//总账户
                        .build();
                OkHttpClient okHttpClient = new OkHttpClient();
                String resData = "";
                try {
                    resData = okHttpClient.newCall(request).execute().body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                    log.error("network question：award hos:" + "user id:" + 1 + "withdraw failed." + sdf.format(new Date()));
                }

                ExchangeApiResDto exchangeApiResDto = gson.fromJson(resData, ExchangeApiResDto.class);
                if (exchangeApiResDto.getId() == null) {
                    log.error("business question:award hos:" + "user id:" + 1 + "withdraw failed." + sdf.format(new Date()));
                } else {
                    // 提现表（后台管理的）
                    insertWithdraw(invitationRecord.getBenefitUser(), walletAndContractAddrDto, exchangeApiResDto);
                    BenefitInviteeUserDto ben = new BenefitInviteeUserDto();
                    ben.setBenefitUserId(invitationRecord.getBenefitUser());
                    ben.setInviteeUserId(invitationRecord.getInviteeUser());
                    successList.add(ben);
                }
            }

            //批量更新邀请记录表
            if (successList.size() > 0) {
                invitationRecordMapper.batchUpdateAwardValidByBenefitUser(successList);
            }
        }
    }


    /**
     * withdraw是一张后台记录的提现记录表（钱包服务将会扫描这张表）
     */
    private void insertWithdraw(Integer benefitUserId, WalletAndContractAddrDto walletAndContractAddrDto, ExchangeApiResDto exchangeApiResDto) {
        Withdraw withdraw = new Withdraw();
        withdraw.setAmount(platformCoinProperties.getAwardAmount());
        withdraw.setCoin(platformCoinProperties.getCoinName());//hos
        withdraw.setStatus(false);
        User adminUser = userMapper.selectByPrimaryKey(1);
        User benefitUser = userMapper.selectByPrimaryKey(benefitUserId);
        withdraw.setSender(adminUser.getWalletAddr());//总账户钱包地址
        withdraw.setReceiver(benefitUser.getWalletAddr());
        withdraw.setTime(new Date());
        withdraw.setContractAddr(walletAndContractAddrDto.getContractAddr());
        // 交易所提现接口返回id
        withdraw.setApiResponseId(exchangeApiResDto.getId());
        // 发起提现的人(总账户)
        withdraw.setReceiverId(1);
        withdrawMapper.insert(withdraw);
    }

}
