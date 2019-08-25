package com.slabs.exchange.model.dto;


import lombok.Data;

@Data
public class WithdrawRequestDto {
    /**
     * 撤单的txid
     */
    private String order_id;
}
