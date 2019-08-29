package com.slabs.exchange.model.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * award_platform_coin
 * @author 
 */
@Data
public class AwardPlatformCoin implements Serializable {

    private Integer id;

    private Integer userId;

    private String coin;

    private BigDecimal amount;

    private String walletAddr;

    private Date time;

    private Boolean status;

    private String contractAddr;

    private String reason;

    private static final long serialVersionUID = 1L;
}