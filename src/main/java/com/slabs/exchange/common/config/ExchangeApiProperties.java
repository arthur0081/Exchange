package com.slabs.exchange.common.config;


import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class ExchangeApiProperties {
    @Value("${exchange-api.host}")
    private String host;

    /**
     * 取消挂单
     */
    @Value("${exchange-api.cancel}")
    private String cancel;

    /**
     * 挂买单和挂卖单
     */
    @Value("${exchange-api.order}")
    private String order;

    /**
     * 提现
     */
    @Value("${exchange-api.withdraw}")
    private String withdraw;

    /**
     * 获取钱包地址
     */
    @Value("${exchange-api.wallet-addr}")
    private String walletAddr;

    /**
     * 获取合约地址
     */
    @Value("${exchange-api.issue-token}")
    private String issueToken;

}
