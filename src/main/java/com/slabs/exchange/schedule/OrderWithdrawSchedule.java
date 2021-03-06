package com.slabs.exchange.schedule;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.slabs.exchange.common.config.ExchangeApiProperties;
import com.slabs.exchange.common.config.ScheduleProperties;
import com.slabs.exchange.common.enums.WithdrawStatusEnum;
import com.slabs.exchange.mapper.back.BoughtAmountMapper;
import com.slabs.exchange.mapper.back.SymbolMapper;
import com.slabs.exchange.model.dto.ExchangeApiResDto;
import com.slabs.exchange.model.dto.WithdrawRequestDto;
import com.slabs.exchange.model.entity.BoughtAmount;
import com.slabs.exchange.model.entity.Symbol;
import com.slabs.exchange.util.JWTUtil;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 撤回挂单逻辑(当项目失效的时候需要的)
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
    @Resource
    private SymbolMapper symbolMapper;
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

           List<Map<String, String>> list = new ArrayList<>();
           List<String> allOrderList = new ArrayList<>();
           for (BoughtAmount ba: boughtAmounts) {
               Map<String, String> symbolNameAndOrderIdMap = new HashMap<>();
               WithdrawRequestDto wd = new WithdrawRequestDto();
               wd.setOrder_id(ba.getOrderId());
               String data = gson.toJson(wd);
               symbolNameAndOrderIdMap.put("orderId", data);
               Symbol symbol = symbolMapper.selectByPrimaryKey(ba.getSymbolId());
               symbolNameAndOrderIdMap.put("symbolName", symbol.getName());

               symbolNameAndOrderIdMap.put("userId", ba.getUserId().toString());
               list.add(symbolNameAndOrderIdMap);
               allOrderList.add(ba.getOrderId());
           }

           List<String> failedList = new ArrayList<>();
           for (Map<String, String> map: list) {
               String orderId = "";
               String symbolName = "";
               String userId = "";
               for(Map.Entry<String, String> entry : map.entrySet()){
                   if ("orderId".equals(entry.getKey())) {
                       orderId = entry.getValue();
                   }
                   if ("symbolName".equals(entry.getKey())) {
                       symbolName = entry.getValue();
                   }
                   if ("userId".equals(entry.getKey())) {
                       userId = entry.getValue();
                   }
               }
               MediaType mediaType = MediaType.parse("text/x-markdown; charset=utf-8");
               Request request = new Request.Builder()
                       .url(exchangeApiProperties.getHost() + exchangeApiProperties.getWithdraw() + symbolName)
                       .post(RequestBody.create(mediaType, orderId))
                       .header("Authorization", "Bearer" + " " + JWTUtil.encode(userId))
                       .build();
               OkHttpClient okHttpClient = new OkHttpClient();
               String resData = "";
               try {
                   resData = okHttpClient.newCall(request).execute().body().toString();
               } catch (IOException e) {
                   e.printStackTrace();
                   log.error("order id:" + orderId + "withdraw failed." + "dateTime:" +  sdf.format(new Date()));
               }
               ExchangeApiResDto ear = gson.fromJson(resData, ExchangeApiResDto.class);
               if (ear.getId() == null) {
                   failedList.add(orderId);
               }
           }

           for (String orderId: failedList) {
               allOrderList.remove(orderId);
           }

           // 根据订单id更新撤销状态为3  (3代表撤回成功的交易)（项目失败的订单需要撤回）
           if (allOrderList.size() > 0) {
               boughtAmountMapper.updateWithdrawStatusByOrderId(allOrderList);
           }
       }
    }
}
