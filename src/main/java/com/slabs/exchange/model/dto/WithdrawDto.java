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
    /**
     * 币种
     */
    private String coin;

    /**
     * 提币数量
     */
    private BigDecimal amount;

    /**
     * 用户提现的资金密码
     */
    private String fundPassword;

    private static final long serialVersionUID = 1L;
}