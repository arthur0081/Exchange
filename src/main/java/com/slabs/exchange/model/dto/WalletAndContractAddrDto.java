package com.slabs.exchange.model.dto;


import lombok.Data;

@Data
public class WalletAndContractAddrDto {

    /**
     * 币持有者的钱包地址
     */
    private String walletAddr;

    /**
     * 币的合约地址
     */
    private String contractAddr;

    /**
     * 币的控制人
     */
    private Integer userId;
}
