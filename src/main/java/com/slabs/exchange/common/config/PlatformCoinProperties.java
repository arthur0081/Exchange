package com.slabs.exchange.common.config;


import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import scala.Int;

import java.math.BigDecimal;

@Data
@Component
public class PlatformCoinProperties {

    @Value("${platform-coin.name}")
    private String coinName;

    @Value("${platform-coin.award-amount}")
    private BigDecimal awardAmount;
}
