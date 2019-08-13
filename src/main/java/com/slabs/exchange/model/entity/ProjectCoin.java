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

    private String name;

    private String verboseName;

    private Long precision;

    private BigDecimal amount;

    private static final long serialVersionUID = 1L;
}