package com.slabs.exchange.model.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SymbolDto {
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

    private String currencyName;

    private String commodityName;

    private static final long serialVersionUID = 1L;
}
