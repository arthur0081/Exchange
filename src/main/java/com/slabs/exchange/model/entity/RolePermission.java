package com.slabs.exchange.model.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * role_permission
 * @author 
 */
@Data
public class RolePermission implements Serializable {
    private Integer id;

    private Integer roleId;

    private Integer permissionId;

    private static final long serialVersionUID = 1L;

}