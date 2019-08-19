package com.slabs.exchange.model.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class HoldCoinUserDto {
    /**
     * 持币数量
     */
    private BigDecimal amount;

    /**
     * 持币百分比
     */
    private String rate;

    /**
     * 用户手机号
     */
    private String account;

    /**
     * 用户钱包地址
     */
    private String walletAddr;
}
