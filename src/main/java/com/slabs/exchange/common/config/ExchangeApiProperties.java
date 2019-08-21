package com.slabs.exchange.common.config;


import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class ExchangeApiProperties {
    @Value("${exchange-api.host}")
    private String host;

    @Value("${exchange-api.withdraw}")
    private String withdraw;

    @Value("${exchange-api.order}")
    private String order;

    @Value("${exchange-api.charge}")
    private String charge;

    @Value("${exchange-api.wallet-addr}")
    private String walletAddr;

}
