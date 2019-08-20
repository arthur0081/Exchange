package com.slabs.exchange.controller;

import com.slabs.exchange.model.common.ResponseBean;
import com.slabs.exchange.model.dto.CountryDto;
import com.slabs.exchange.model.dto.UserDto;
import com.slabs.exchange.service.IUserService;
import com.slabs.exchange.util.CountryUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 身份认证
 */
@RestController
@RequestMapping("identity")
@Api(value = "身份认证", description = "身份认证模块：身份认证相关接口")
public class IdentityController {

    @Resource
    private IUserService userService;

    /**
     * 身份认证
     */
    @PostMapping("update")
    @ApiOperation(value = "修改")
    public ResponseBean identityUpdate(UserDto userDto) {
        return userService.identityUpdate(userDto);
    }

    /**
     * 预修改（回显）(可以从Shiro中获取当前登陆者的信息)
     */
    @PostMapping("pre-update")
    @ApiOperation(value = "预修改")
    public ResponseBean preUpdate() {
        //return userService.preUpdate();
        return null;
    }

    /**
     * 得到所有国籍
     */
    @PostMapping("get-all-countries")
    @ApiOperation(value = "获取所有国籍")
    public ResponseBean getAllCountries() {
        // 这个方法是单例模式
        CountryUtil.getInstance();
        List<CountryDto> dto = CountryUtil.countryDtos;
        return new ResponseBean(200, "", dto);
    }

}
