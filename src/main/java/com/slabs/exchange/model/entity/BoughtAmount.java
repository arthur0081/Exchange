package com.slabs.exchange.model.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * bought_amount
 * @author 
 */
@Data
public class BoughtAmount implements Serializable {

    private Integer id;

    private Integer userId;

    private Integer projectId;

    private Integer coinId;

    private Date createTime;

    private String orderId;

    private Integer withdraw;

    private BigDecimal coinAmount;

    private BigDecimal hosAmount;

    private Integer symbolId;

    private static final long serialVersionUID = 1L;
}