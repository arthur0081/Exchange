package com.slabs.exchange.model.dto;


import lombok.Data;

import java.math.BigDecimal;

/**
 * 挂买单 dto
 */
@Data
public class OrderDto {
    /**
     * 默认挂单是  buy
     */
    private String side;

    /**
     * 初始价格
     */
    private BigDecimal price;

    /**
     * 购买数量
     */
    private BigDecimal amount;
}
