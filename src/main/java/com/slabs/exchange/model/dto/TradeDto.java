package com.slabs.exchange.model.dto;


import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class TradeDto {

    /**
     * 用户账户
     */
    private String account;

    /**
     * 币对名称
     */
    private String symbol;

    private String side;

    private BigDecimal price;

    private BigDecimal amount;

    private BigDecimal fee;

    private Date time;

    /**
     * 币对名称
     */
    private String name;


    /**
     * 币对id
     */
    private Integer symbolId;

    /**
     * 24小时的交易总数
     */
    private BigDecimal sums;

}
