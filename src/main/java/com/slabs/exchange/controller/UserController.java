package com.slabs.exchange.controller;


import com.slabs.exchange.model.common.ResponseBean;
import com.slabs.exchange.model.dto.PageParamDto;
import com.slabs.exchange.model.dto.UserDto;
import com.slabs.exchange.service.IUserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 权限管理
 * 这里的逻辑添加所有前台用户
 */
@RestController
@RequestMapping("system/user")
public class UserController {
    @Resource
    private IUserService userService;

    /**
     * 新增用户（新增的时候带上角色信息）
     */
    @PostMapping("save")
    public ResponseBean save(@RequestBody UserDto userDto) {
        return userService.save(userDto);
    }

    /**
     * preUpdate 根据用户id查询用户信息及其系统中的所有角色
     */
    @PostMapping("preUpdate")
    public ResponseBean preUpdate(@RequestBody Integer userId) {

        return userService.selectOneById(userId);
    }

    /**
     * 更新用户
     */
    @PostMapping("update")
    public ResponseBean update(@RequestBody UserDto sysuserDto) {
        return userService.update(sysuserDto);
    }

    /**
     * 列表分页查询
     */
    @PostMapping("list")
    public ResponseBean list(@RequestBody PageParamDto pageParamDto) {
        return userService.list(pageParamDto);
    }

    /**
     * 重置登陆密码
     */
    @PostMapping("reset-login-password")
    public ResponseBean resetLoginPassword() {
        return userService.resetLoginPassword();
    }

    /**
     * 重置资金密码
     */
    @PostMapping("reset-fund-password")
    public ResponseBean resetFundPassword() {
        return userService.resetFundPassword();
    }

    /**
     * 我的信息
     */
    @PostMapping("my-info")
    public ResponseBean getMyInfo() {
        return userService.getMyInfo();
    }

}
