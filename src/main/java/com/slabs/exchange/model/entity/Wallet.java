package com.slabs.exchange.model.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * wallet
 * @author 
 */
@Data
public class Wallet implements Serializable {
    private Long id;

    private Long userId;

    private String addr;

    private static final long serialVersionUID = 1L;
}