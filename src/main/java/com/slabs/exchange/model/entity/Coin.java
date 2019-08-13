package com.slabs.exchange.model.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * t_coin
 * @author 
 */
@Data
public class Coin implements Serializable {
    private Integer id;

    private String name;

    private String verboseName;

    private Integer precision;

    private static final long serialVersionUID = 1L;
}