package com.slabs.exchange.model.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * withdraw
 * @author 
 */
@Data
public class Withdraw implements Serializable {

    private Integer id;

    private Integer userId;

    private String coin;

    private BigDecimal amount;

    private String txid;

    private Date time;

    private Boolean status;

    private static final long serialVersionUID = 1L;
}