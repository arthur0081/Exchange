package com.slabs.exchange.common.config;


import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import scala.Int;

@Data
@Component
public class PlatformCoinProperties {

    @Value("${platform-coin.name}")
    private String coinName;

    @Value("${platform-coin.award-amount}")
    private Integer awardAmount;
}
