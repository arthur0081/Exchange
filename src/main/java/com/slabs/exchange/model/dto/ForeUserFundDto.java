package com.slabs.exchange.model.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ForeUserFundDto {

    private String coin;

    private String name;

    private BigDecimal available;

    private BigDecimal lock;
}
