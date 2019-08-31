package com.slabs.exchange.util;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CancelallOrderUtil {
    @Autowired
    private RedisUtil redisUtil;


    /**
     * 根据币对名称取消该币对的所有挂单
     */
    public void cancelAllOrder(String symbolName) {

    }
}
