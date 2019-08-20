package com.slabs.exchange.controller.back;

import com.slabs.exchange.model.common.ResponseBean;
import com.slabs.exchange.model.dto.AuditDto;
import com.slabs.exchange.model.dto.PageParamDto;
import com.slabs.exchange.model.dto.ProjectDto;
import com.slabs.exchange.service.back.IProjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 发布项目
 */
@RestController
@RequestMapping("project")
@Api(value = "项目", description = "项目模块：项目相关接口")
public class ProjectController {

    @Resource
    private IProjectService projectService;

    /**
     * 新增项目
     */
    @PostMapping("insert")
    @ApiOperation(value = "插入")
    public ResponseBean insert(@RequestBody ProjectDto projectDto) {
        return projectService.insert(projectDto);
    }

    /**
     * preUpdate
     */
    @PostMapping("pre-update")
    @ApiOperation(value = "预修改")
    public ResponseBean preUpdate(@RequestBody Long projectId) {
        return projectService.preUpdate(projectId);
    }

    /**
     * update
     */
    @PostMapping("update")
    @ApiOperation(value = "更新")
    public ResponseBean update(@RequestBody ProjectDto projectDto) {
        return projectService.update(projectDto);
    }

    /**
     * list
     */
    @PostMapping("list")
    @ApiOperation(value = "列表")
    public ResponseBean list(@RequestBody PageParamDto pageParamDto) {
        return projectService.list(pageParamDto);
    }

    /**
     * 超级管理员进行审核
     */
    @PostMapping("audit")
    @ApiOperation(value = "超级管理员审核")
    public ResponseBean audit(@RequestBody AuditDto auditDto) {
        return projectService.audit(auditDto);
    }

}
