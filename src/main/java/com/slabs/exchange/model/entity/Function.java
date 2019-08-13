package com.slabs.exchange.model.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * function
 * @author 
 */
@Data
public class Function implements Serializable {
    private Long functionId;

    private Long parentId;

    private String name;

    private Short displayOrder;

    private String isDel;

    private Long createId;

    private Date createTime;

    private Long modifyId;

    private Date modifyTime;

    private static final long serialVersionUID = 1L;
}