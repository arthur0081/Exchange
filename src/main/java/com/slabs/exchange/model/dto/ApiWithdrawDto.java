package com.slabs.exchange.model.dto;


import lombok.Data;

import java.math.BigDecimal;

/**
 * 用户提现的时候请求交易系统的提现接口参数
 */
@Data
public class ApiWithdrawDto {

    /**
     * 币种简称
     */
    private String coin;

    /**
     * 操作方式（deposit, redeem）
     */
    private String operation;

    /**
     * 提取的数量
     */
    private BigDecimal amount;

    /**
     * ？
     */
    private String txid;
}
