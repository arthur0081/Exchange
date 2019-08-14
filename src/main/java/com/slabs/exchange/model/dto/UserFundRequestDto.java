package com.slabs.exchange.model.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * t_user_fund_request
 * @author 
 */
@Data
public class UserFundRequestDto implements Serializable {

    private Integer id;

    private String coin;

    private Integer debitor;

    private Integer creditor;

    private BigDecimal amount;

    private String txid;

    private String operation;

    private Object time;

    private Long pgTxid;

    private String extra;

    private static final long serialVersionUID = 1L;


}