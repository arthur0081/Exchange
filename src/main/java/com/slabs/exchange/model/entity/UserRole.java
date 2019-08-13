package com.slabs.exchange.model.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * user_role
 * @author 
 */
@Data
public class UserRole implements Serializable {
    private Integer id;

    private Integer userId;

    private Integer roleId;

    private static final long serialVersionUID = 1L;
}