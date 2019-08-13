package com.slabs.exchange.model.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * permission
 * @author 
 */
@Data
public class Permission implements Serializable {
    private Integer id;

    private String name;

    private String perCode;

    private static final long serialVersionUID = 1L;
}