package com.slabs.exchange.controller.fore;

import com.slabs.exchange.model.common.ResponseBean;
import com.slabs.exchange.model.dto.BuyDto;
import com.slabs.exchange.model.dto.PageParamDto;
import com.slabs.exchange.service.back.IProjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 前台 项目列表(此controller只针对稳定区)
 */
@RestController
@RequestMapping("fore-project")
@Api(value = "前台项目列表", description = "前台项目列表模块：前台项目相关接口")
public class ForeProjectController {

    @Resource
    private IProjectService projectService;

    /**
     * 前台列表查询（稳定区）
     */
    @PostMapping("fore-list")
    @ApiOperation(value = "前台列表查询（稳定区）")
    public ResponseBean getForeProjectList(@RequestBody PageParamDto pageParamDto) {
        return projectService.getForeProjectList(pageParamDto);
    }

    /**
     * 前台： 详情
     */
    @GetMapping("detail")
    @ApiOperation(value = "前台： 项目详情")
    public ResponseBean getForeProjectDetail(Integer projectId) {
        return projectService.getForeProjectDetail(projectId);
    }

    /**
     * 前台： 买入
     */
    @PostMapping("buy")
    @ApiOperation(value = "前台： 买入")
    public ResponseBean buy(@RequestBody BuyDto buyDto) {
        return projectService.buy(buyDto);
    }

}
