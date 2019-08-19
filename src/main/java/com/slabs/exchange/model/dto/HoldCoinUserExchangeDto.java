package com.slabs.exchange.model.dto;


import lombok.Data;

/**
 * 持币用户换手率
 */
@Data
public class HoldCoinUserExchangeDto {
    /**
     * 币种
     */
    private String coin;
    /**
     * 持币用户数
     */
    private Integer number;

    /**
     * 24H换手率
     */
    private String hourExchangeRate;
}
