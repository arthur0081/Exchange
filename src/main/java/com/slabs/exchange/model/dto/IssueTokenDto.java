package com.slabs.exchange.model.dto;


import lombok.Data;

import java.math.BigDecimal;

/**
 * 请求发币dto
 */
@Data
public class IssueTokenDto {
    /**
     * 币简称
     */
    private String name;

    /**
     * 钱包地址
     */
    private String address;

    /**
     * 币总数
     */
    private BigDecimal totalAmount;

}
