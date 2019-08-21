package com.slabs.exchange.model.dto;


import lombok.Data;

/**
 * 调用交易引擎返回的数据
 */
@Data
public class ExchangeApiResDto {
    /**
     * 交易引擎返回的都是交易id(txid的字符串)
     */
    private String id;

}
