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

    private static final long serialVersionUID = 1L;
}