package com.slabs.exchange.model.dto;

import java.math.BigDecimal;

public class DepositDto {
    //实体转json
    private String coin;

    private String operation;

    private BigDecimal amount;

    private String txid;

    // 后台调用钱包充值接口，钱包调用黄毅的充值接口。

    // 充值 提现 都是 钱包 调用黄毅的接口

    //

}
