package com.slabs.exchange.model.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * withdraw
 * @author 
 */
@Data
public class WithdrawDto implements Serializable {
    private String coin;

    private BigDecimal amount;

    private String fundPassword;

    /**
     * 钱包地址，是任意的地址。可能是给自己的钱包地址或者其他人的钱包地址
     */
    private String walletAddr;


    private static final long serialVersionUID = 1L;
}