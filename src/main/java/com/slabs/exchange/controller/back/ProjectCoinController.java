package com.slabs.exchange.controller.back;

import com.slabs.exchange.model.common.ResponseBean;
import com.slabs.exchange.model.dto.PageParamDto;
import com.slabs.exchange.model.dto.ProjectCoinDto;
import com.slabs.exchange.service.back.IProjectCoinService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 项目币种
 */
@RestController
@RequestMapping("project-coin")
@Api(value = "项目币", description = "项目币：项目币列表查询功能")
public class ProjectCoinController {

    @Resource
    private IProjectCoinService projectCoinService;

    /**
     * 预修改（准备修改时的回显）
     */

    @PostMapping("pre-update")
    @ApiOperation(value = "预修改")
    public ResponseBean preUpdate(@RequestBody Integer coinId) {
        return projectCoinService.preUpdate(coinId);
    }

    /**
     * 修改
     */
    @PostMapping("update")
    @ApiOperation(value = "修改")
    public ResponseBean update(@RequestBody ProjectCoinDto projectCoinDto) {
        return projectCoinService.update(projectCoinDto);
    }

    /**
     * 新增项目币
     */
    @PostMapping("insert")
    @ApiOperation(value = "新增")
    public ResponseBean insert(@RequestBody ProjectCoinDto projectCoinDto) {
        return projectCoinService.insert(projectCoinDto);
    }

    /**
     * 列表查询
     */
    @PostMapping("list")
    @ApiOperation(value = "列表")
    public ResponseBean list(@RequestBody PageParamDto pageParamDto) {
        return projectCoinService.list(pageParamDto);
    }

    /**
     *  得到所有的商品货币（本质就是项目币）
     */
    @PostMapping("getProjectCoins")
    @ApiOperation(value = "得到所有项目币")
    public ResponseBean getProjectCoins() {
        return projectCoinService.getProjectCoins();
    }


    /**
     * 所有有效的币对
     */
    @ApiOperation(value = "获取稳定币对")
    @PostMapping("stable-symbol")
    public ResponseBean getStableSymbols() {
        return projectCoinService.getStableSymbols();
    }
}
