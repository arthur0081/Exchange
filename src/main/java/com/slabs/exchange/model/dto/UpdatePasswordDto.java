package com.slabs.exchange.model.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class UpdatePasswordDto implements Serializable {
    /**
     * 原来的登陆密码
     */
    private String loginPassword;
    /**
     * 新的登陆密码
     */
    private String newLoginPassword;

    /**
     * 原来的资金密码
     */
    private String fundPassword;

    /**
     * 新的资金密码
     */
    private String newFundPassword;

    private static final long serialVersionUID = 1L;
}
