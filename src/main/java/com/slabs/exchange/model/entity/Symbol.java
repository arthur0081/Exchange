package com.slabs.exchange.model.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * t_symbol
 * @author 
 */
@Data
public class Symbol implements Serializable {
    private Integer id;

    private String name;

    private Integer currency;

    private Integer commodity;

    private BigDecimal priceUnit;

    private BigDecimal amountUnit;

    private BigDecimal minAmount;

    private BigDecimal minValue;

    private BigDecimal initPrice;

    private BigDecimal takerFee;

    private BigDecimal makerFee;

    private static final long serialVersionUID = 1L;
}