package com.slabs.exchange.controller;

import com.slabs.exchange.model.common.ResponseBean;
import com.slabs.exchange.model.dto.OauthInfoDto;
import com.slabs.exchange.model.dto.UserDto;
import com.slabs.exchange.service.IUserService;
import com.slabs.exchange.util.ShiroUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/")
@Api(value = "登录操作", description = "登录模块：用户注册登录操作相关接口")
public class LoginController {

    @Autowired
    private IUserService userService ;

    @PostMapping("login")
    @ApiOperation(value = "登录")
    public ResponseBean login(@RequestBody UserDto userDto) {
        OauthInfoDto oauthInfoDto = userService.login(userDto);
        return  new ResponseBean(200, "", oauthInfoDto);
    }

    @GetMapping("/logout")
    @ApiOperation(value = "退出登陆")
    public ResponseBean logout() {
        ShiroUtils.logout();
        return new ResponseBean(200, "", null);
    }
}
