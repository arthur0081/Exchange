package com.slabs.exchange.model.dto;

import lombok.Data;

@Data
public class WalletResponseDto {
    //如果响应码是200的话，body中就是项目的地址信息
    private String body;

    // 只有成功或者失败（SUCCESS，FAILED）
    private String msg;

    // 响应码
    private Integer code;

}
