package com.slabs.exchange.controller;

import com.slabs.exchange.model.common.ResponseBean;
import com.slabs.exchange.model.dto.UserDto;
import com.slabs.exchange.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 前台用户注册
 */
@RestController
@RequestMapping("user")
@Api(value = "注册", description = "注册模块：注册相关接口")
public class RegisterController {

    @Resource
    private IUserService userService;
    /**
     * 前台用户注册（给默认角色）
     */
    @PostMapping("register")
    @ApiOperation(value = "注册")
    public ResponseBean register(@RequestBody UserDto userDto) {
        return userService.register(userDto);
    }
}
