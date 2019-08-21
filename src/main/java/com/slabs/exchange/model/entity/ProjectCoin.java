package com.slabs.exchange.model.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * project_coin
 * @author 
 */
@Data
public class ProjectCoin implements Serializable {
    private Long id;

    private BigDecimal amount;

    private String contractAddr;

    private Integer userId;

    private Integer coinId;

    private String coinType;

    private static final long serialVersionUID = 1L;
}