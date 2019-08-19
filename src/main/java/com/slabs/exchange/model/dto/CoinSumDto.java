package com.slabs.exchange.model.dto;


import lombok.Data;

import java.math.BigDecimal;

@Data
public class CoinSumDto {

    /**
     *  币名称
     */
    private String coin;

    /**
     * 币总量
     */
    private BigDecimal sums;
}
