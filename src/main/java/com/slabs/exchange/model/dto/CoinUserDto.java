package com.slabs.exchange.model.dto;

import lombok.Data;

/**
 * 持币用户换手率
 */
@Data
public class CoinUserDto {
    /**
     * 币种
     */
    private String coin;

    /**
     * 用户数
     */
    private Integer number;
}
