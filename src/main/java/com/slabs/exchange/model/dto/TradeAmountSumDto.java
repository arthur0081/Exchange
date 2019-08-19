package com.slabs.exchange.model.dto;


import lombok.Data;

import java.math.BigDecimal;

@Data
public class TradeAmountSumDto {
    /**
     * 币对
     */
    private Long symbolId;

    /**
     * 24小时内的交易总数
     */
    private BigDecimal sums;

}
