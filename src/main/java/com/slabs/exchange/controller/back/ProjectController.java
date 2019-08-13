package com.slabs.exchange.controller.back;

import com.slabs.exchange.model.common.ResponseBean;
import com.slabs.exchange.model.dto.AuditDto;
import com.slabs.exchange.model.dto.PageParamDto;
import com.slabs.exchange.model.dto.ProjectDto;
import com.slabs.exchange.service.back.IProjectService;
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
public class ProjectController {

    @Resource
    private IProjectService projectService;

    /**
     * 新增项目
     */
    @PostMapping("insert")
    public ResponseBean insert(@RequestBody ProjectDto projectDto) {
        return projectService.insert(projectDto);
    }

    /**
     * preUpdate
     */
    @PostMapping("pre-update")
    public ResponseBean preUpdate(@RequestBody Long projectId) {
        return projectService.preUpdate(projectId);
    }

    /**
     * update
     */
    @PostMapping("update")
    public ResponseBean update(@RequestBody ProjectDto projectDto) {
        return projectService.update(projectDto);
    }

    /**
     * list
     */
    @PostMapping("list")
    public ResponseBean list(@RequestBody PageParamDto pageParamDto) {
        return projectService.list(pageParamDto);
    }

    /**
     * 超级管理员进行审核
     */
    @PostMapping("audit")
    public ResponseBean audit(@RequestBody AuditDto auditDto) {
        return projectService.audit(auditDto);
    }

}
