package com.slabs.exchange.model.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ForeUserFundDto {

    /**
     * 币种
     */
    private String coin;

    /**
     * USDT估值（其他币换算成USDT）
     */
    private BigDecimal amount;

    /**
     * 可用余额
     */
    private BigDecimal available;

    /**
     * 冻结金额
     */
    private BigDecimal lock;

}
