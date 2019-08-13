package com.slabs.exchange.controller.fore;

import com.slabs.exchange.model.common.ResponseBean;
import com.slabs.exchange.model.dto.BuyDto;
import com.slabs.exchange.model.dto.PageParamDto;
import com.slabs.exchange.service.back.IProjectService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 前台 项目列表
 */
@RestController
@RequestMapping("fore-project")
public class ForeProjectController {

    @Resource
    private IProjectService projectService;

    /**
     * 前台列表查询（保本区和创新区）默认保本区
     */
    @PostMapping("fore-list")
    public ResponseBean getForeProjectList(@RequestBody PageParamDto pageParamDto) {
        return projectService.getForeProjectList(pageParamDto);
    }

    /**
     * 前台： 详情
     */
    @PostMapping("detail")
    public ResponseBean getForeProjectDetail(@RequestBody Long projectId) {
        return projectService.getForeProjectDetail(projectId);
    }

    /**
     * 前台： 买入
     */
    @PostMapping("buy")
    public ResponseBean buy(@RequestBody BuyDto buyDto) {
        return projectService.buy(buyDto);
    }

}
