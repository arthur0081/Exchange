package com.slabs.exchange.model.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class PermissionDto implements Serializable {
    private Integer id;

    private String name;

    private String perCode;

    private static final long serialVersionUID = 1L;
}
