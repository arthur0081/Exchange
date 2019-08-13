package com.slabs.exchange.controller;


import com.slabs.exchange.model.common.ResponseBean;
import com.slabs.exchange.model.dto.RoleDto;
import com.slabs.exchange.service.IRoleService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("system/role")
public class RoleController {
    @Resource
    private IRoleService roleService;

    /**
     * 新增角色
     */
    @PostMapping("insert")
    public ResponseBean insert(@RequestBody RoleDto roleDto) {
        return roleService.insert(roleDto);
    }

    /**
     * preUpdate
     */
    @PostMapping("preUpdate")
    public ResponseBean preUpdate(@RequestBody RoleDto roleDto) {
        return roleService.preUpdate(roleDto);
    }

    /**
     * 修改（角色名称和角色对应的功能）
     */
    @PostMapping("update")
    public ResponseBean update(@RequestBody RoleDto roleDto) {
        return roleService.update(roleDto);
    }



    /**
     * 得到全部的角色
     */
    @PostMapping("list")
    public ResponseBean getRoles() {
        return roleService.getRoles();
    }

}
