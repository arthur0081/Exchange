package com.slabs.exchange.model.entity;

import org.apache.ibatis.annotations.Mapper;

import java.io.Serializable;

/**
 * role
 * @author 
 */
@Mapper
public class Role implements Serializable {
    private Integer id;

    private String name;

    private String remark;

    private static final long serialVersionUID = 1L;
}