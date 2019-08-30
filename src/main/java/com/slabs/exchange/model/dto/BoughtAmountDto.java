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


    /**
     * 用户账户
     */
    private String  account;

    /**
     * 用户钱包地址
     */
    private String walletAddr;

    /**
     * 用户usdt认购额度
     */
    private BigDecimal usdtAmount;

    /**
     * 认购项目币数量
     */
    private BigDecimal coinAmount;


}
