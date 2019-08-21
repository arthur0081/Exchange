package com.slabs.exchange.model.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class BuyDto {
    /**
     * 项目id
     */
    private Integer projectId;

    /**
     * 认购份额
     */
    private BigDecimal boughtAmount;

    /**
     * 首发价格
     */
    private BigDecimal initPrice;

}
