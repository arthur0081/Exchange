package com.slabs.exchange.service.impl;

import com.slabs.exchange.mapper.PermissionMapper;
import com.slabs.exchange.mapper.RoleMapper;
import com.slabs.exchange.mapper.RolePermissionMapper;
import com.slabs.exchange.mapper.ext.PermissionExtMapper;
import com.slabs.exchange.model.common.ResponseBean;
import com.slabs.exchange.model.dto.RoleDto;
import com.slabs.exchange.model.entity.Permission;
import com.slabs.exchange.model.entity.Role;
import com.slabs.exchange.model.entity.RolePermission;
import com.slabs.exchange.service.BaseService;
import com.slabs.exchange.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RoleServiceImpl extends BaseService implements IRoleService {
    @Resource
    private RoleMapper roleMapper;
    @Resource
    private RolePermissionMapper rolePermissionMapper;
    @Resource
    private PermissionMapper permissionMapper;

    @Autowired
    private PermissionExtMapper permissionExtMapper;

    /**
     * 新增角色
     */
    @Override
    public ResponseBean insert(RoleDto roleDto) {
        // 数据传输对象映射实体
        Role role = map(roleDto, Role.class);
        // 插入角色基础信息
        roleMapper.insert(role);
        return new ResponseBean(200, "", null);
    }

    /**
     * 修改角色信息及角色对应的功能权限
     */
    @Override
    public ResponseBean update(RoleDto roleDto) {
        // 数据传输对象映射实体
        Role role = map(roleDto, Role.class);
        // 更新角色基础信息
        roleMapper.updateByPrimaryKeySelective(role);
        // 构建角色对应的功能关系
        List<RolePermission> rolePermissions = buildRolePermissions(roleDto);
        // 先清除原来角色对应的功能关系
        rolePermissionMapper.deleteByRoleId(roleDto.getId());
        // 再批量插入角色对应的功能关系
        rolePermissionMapper.batchInsert(rolePermissions);

        return new ResponseBean(200, "", null);
    }

    /**
     * 构建角色对应的功能关系
     */
    private List<RolePermission> buildRolePermissions(RoleDto roleDto) {
        List<RolePermission> rolePermissionList = new ArrayList<>();
        List<Integer> permissionList = roleDto.getPermissionList();
        Integer roleId = roleDto.getId();
        for (Integer in: permissionList) {
            RolePermission rolePermission = new RolePermission();
            rolePermission.setRoleId(roleId);
            rolePermission.setPermissionId(in);
            rolePermissionList.add(rolePermission);
        }
        return rolePermissionList;
    }


    /**
     * 获取更新前的回显数据信息
     */
    @Override
    public ResponseBean preUpdate(RoleDto roleDto) {
        // 获取角色对应的功能信息
        List<RolePermission> rolePermissions = rolePermissionMapper.selectByRoleId(roleDto.getId());

        // 获取所有的功能信息
        List<Permission> permissionList = permissionMapper.selectAll();

        // 构建响应信息
        Map<String, Object> data = new HashMap<>();
        data.put("rolePermissions", rolePermissions);
        data.put("permissionList", permissionList);

        return new ResponseBean(200, "", data);
    }


    /**
     * 对角色进行列表查询
     * 系统默认4类角色，（超级管理员，普通管理员，这两个是后台用户），（项目方，普通用户，这两个是前台用户）
     */
    @Override
    public ResponseBean getRoles() {
        List<Role> roleList = roleMapper.getRoles();
        return new ResponseBean(200, "", roleList);
    }


    @Override
    public List<String> queryNamesByRoleId(Integer userId) {
        return permissionExtMapper.queryNamesByRoleId(userId);
    }

}
