package com.slabs.exchange.model.dto;


import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class OrderListDto {
    // 币对名称
    private String name;
    // sell/buy
    private String side;
    // 总数
    private BigDecimal amount;
    // 价格
    private BigDecimal price;
    // 用户账户
    private String account;
    // 日期
    private Date time;
}
