package com.slabs.exchange.schedule;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.slabs.exchange.common.config.ExchangeApiProperties;
import com.slabs.exchange.common.config.ScheduleProperties;
import com.slabs.exchange.mapper.fore.UserFundRequestMapper;
import com.slabs.exchange.mapper.fore.WithdrawMapper;
import com.slabs.exchange.model.entity.UserFundRequest;
import com.slabs.exchange.model.entity.Withdraw;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * 定时扫描表中的txid字段，看txid是否有效的
 * 提现定时任务
 */
@Component
@Slf4j
public class WithdrawSchedule {
    private static final Gson gson = new GsonBuilder().create();
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    @Resource
    private ExchangeApiProperties exchangeApiProperties;
    @Resource
    private ScheduleProperties scheduleProperties;
    @Resource
    private WithdrawMapper withdrawMapper;
    @Resource
    private UserFundRequestMapper userFundRequestMapper;

    @Scheduled(cron = "0 0/10 * * * ?")
    public void withdraw() {
        while (true) {
//            //1. 扫描表找到表中无效的提现交易记录
//            List<Withdraw> withdrawList =  withdrawMapper.getStatusIsFalse(scheduleProperties.getNum());
//            if (withdrawList == null || withdrawList.size() == 0) {
//                return;
//            }
//            //2. 主动查询txid是否有效
//            // TODO
//            //3. 如果有效，更新withdraw表中的status
//            //4. 然后提现操作的一条数据插入到t_user_fund_request中
//            UserFundRequest ufr = new UserFundRequest();
//            // 构建实体对象
//            ufr.setOperation("withdraw");// 提现
//            userFundRequestMapper.insert(ufr);
        }

    }

}
