package com.slabs.exchange.model.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * t_trade
 * @author 
 */
@Data
public class Trade implements Serializable {

    private Long id;

    private Long takerId;

    private Integer userId;

    private String side;

    private BigDecimal price;

    private BigDecimal amount;

    private BigDecimal fee;

    private Date time;

    private Integer symbolId;

    private static final long serialVersionUID = 1L;
}