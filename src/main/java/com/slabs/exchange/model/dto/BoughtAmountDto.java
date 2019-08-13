package com.slabs.exchange.model.dto;


import lombok.Data;

import java.math.BigDecimal;

@Data
public class BoughtAmountDto {
    /**
     * 项目id
     */
    private Integer projectId;

    /**
     * 项目当前募集到的总额（USDT）
     */
    private BigDecimal amount;


}
