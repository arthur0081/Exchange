package com.slabs.exchange.schedule;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.slabs.exchange.common.config.ExchangeApiProperties;
import com.slabs.exchange.common.config.ScheduleProperties;
import com.slabs.exchange.common.enums.WithdrawStatusEnum;
import com.slabs.exchange.mapper.fore.UserFundRequestMapper;
import com.slabs.exchange.mapper.fore.WithdrawMapper;
import com.slabs.exchange.model.dto.WithdrawRequestDto;
import com.slabs.exchange.model.entity.BoughtAmount;
import com.slabs.exchange.model.entity.UserFundRequest;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 充值任务逻辑
 */
@Component
@Slf4j
public class DepositSchedule {

    private static final Gson gson = new GsonBuilder().create();
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    @Resource
    private ExchangeApiProperties exchangeApiProperties;
    @Resource
    private ScheduleProperties scheduleProperties;
    @Resource
    private UserFundRequestMapper userFundRequestMapper;

    /**
     * 撤回挂单
     */
    @Scheduled(cron = "0 0/10 * * * ?")
    public void deposit() {
        //1. 去t_user_fund_request表中获取最大编号

        //2. 主动去请求这个编号之后的交易(TODO请求第三方接口)

        //3. 这些交易一定是充值的

        //4. 获取到这些交易直接插入到t_user_fund_requst表中即可

        UserFundRequest ufr = new UserFundRequest();
        ufr.setOperation("deposit");
        userFundRequestMapper.insert(ufr);

    }
}
