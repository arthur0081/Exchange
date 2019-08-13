package com.slabs.exchange.model.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * t_user_fund
 * @author 
 */
@Data
public class UserFund implements Serializable {

    private Long id;

    private Integer userId;

    private String coin;

    private BigDecimal amount;

    private BigDecimal orderLocked;

    private BigDecimal withdrawLocked;

    private static final long serialVersionUID = 1L;
}