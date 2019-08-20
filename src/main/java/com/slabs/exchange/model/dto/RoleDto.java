package com.slabs.exchange.model.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class RoleDto implements Serializable {
    private Integer id;

    private String name;

    private String remark;

    private List<Integer> permissionList;

    private static final long serialVersionUID = 1L;
}
