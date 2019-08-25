package com.slabs.exchange.schedule;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.slabs.exchange.common.config.ExchangeApiProperties;
import com.slabs.exchange.common.config.ScheduleProperties;
import com.slabs.exchange.common.enums.WithdrawStatusEnum;
import com.slabs.exchange.mapper.back.BoughtAmountMapper;
import com.slabs.exchange.model.dto.WithdrawRequestDto;
import com.slabs.exchange.model.entity.BoughtAmount;
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
 * 撤回挂单逻辑
 */
@Component
@Slf4j
public class OrderWithdrawSchedule {
    private static final Gson gson = new GsonBuilder().create();
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    @Resource
    private BoughtAmountMapper boughtAmountMapper;
    @Resource
    private ExchangeApiProperties exchangeApiProperties;
    @Resource
    private ScheduleProperties scheduleProperties;
    /**
     * 撤回挂单
     */
    @Scheduled(cron = "0 0/10 * * * ?")
    public void withdrawOrder() {
        // todo 调用第三方接口，撤回挂单
        //  （/cancel/bvp_usdt） post {"order_id":"string"}
        // 响应 ： {"id":"string"}
       while (true) {
           List<BoughtAmount> boughtAmounts =  boughtAmountMapper.getWithdrawsByNum(scheduleProperties.getNum(), Integer.valueOf(WithdrawStatusEnum.WITHDRAW_NEED.getKey()));
           if (boughtAmounts.size() == 0) {
               return;
           }

           List<String> orderIds = new ArrayList<>();
           for (BoughtAmount ba: boughtAmounts) {
               WithdrawRequestDto wd = new WithdrawRequestDto();
               wd.setOrder_id(ba.getOrderId());
               String data = gson.toJson(wd);
               orderIds.add(data);
           }

           List<String> succeedList = new ArrayList<>();
           for(String orderId: orderIds) {
               MediaType mediaType = MediaType.parse("text/x-markdown; charset=utf-8");
               Request request = new Request.Builder()
                       .url(exchangeApiProperties.getHost() + exchangeApiProperties.getWithdraw())
                       .post(RequestBody.create(mediaType, orderId))
                       .build();
               OkHttpClient okHttpClient = new OkHttpClient();
               try {
                   Response response = okHttpClient.newCall(request).execute();
               } catch (IOException e) {
                   e.printStackTrace();
                   log.error("order id:" + orderId + "withdraw failed." + sdf.format(new Date()));
               }
               succeedList.add(orderId);
           }

           // 根据订单id更新撤销状态为3
           boughtAmountMapper.updateWithdrawStatusByOrderId(succeedList);
       }

    }

}
