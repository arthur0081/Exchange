package com.slabs.exchange.schedule;


import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 撤回挂单逻辑
 */
@Component
@Slf4j
public class OrderWithdrawSchedule {


    /**
     * 撤回挂单
     */
    @Scheduled(cron = "0 0/10 * * * ?")
    public void withdrawOrder() {
        // 一旦失效，将要撤回这个项目的所有挂单
        // todo 调用第三方接口，撤回挂单
        //  （/cancel/bvp_usdt） post {"order_id":"string"}
        // 响应 ： {"id":"string"}

        //boughtAmount的withdraw（默认是0） 1为需要撤回  2是已经撤回的

        MediaType mediaType = MediaType.parse("text/x-markdown; charset=utf-8");
        String requestBody = "{}";
        Request request = new Request.Builder()
                .url("充值接口ip地址")
                .post(RequestBody.create(mediaType, requestBody))
                .build();
        OkHttpClient okHttpClient = new OkHttpClient();
        try {
            Response response = okHttpClient.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
            log.error("");
        }

    }

}
