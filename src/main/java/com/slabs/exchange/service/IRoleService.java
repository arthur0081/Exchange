package com.slabs.exchange.service;

import com.slabs.exchange.model.common.ResponseBean;
import com.slabs.exchange.model.dto.RoleDto;

import java.util.List;

public interface IRoleService {

    ResponseBean insert(RoleDto roleDto);

    ResponseBean update(RoleDto roleDto);

    ResponseBean getRoles();

    ResponseBean preUpdate(RoleDto roleDto);

    List<String> queryNamesByRoleId(Long userId);
}
