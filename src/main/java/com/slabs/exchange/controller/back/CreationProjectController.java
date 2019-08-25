package com.slabs.exchange.controller.back;


import com.slabs.exchange.model.common.ResponseBean;
import com.slabs.exchange.model.dto.AuditDto;
import com.slabs.exchange.model.dto.PageParamDto;
import com.slabs.exchange.model.dto.ProjectDto;
import com.slabs.exchange.service.back.ICreationProjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("creation-project")
@Api(value = "创新项目", description = "创新项目：创新项目相关接口")
public class CreationProjectController {
    @Resource
    private ICreationProjectService creationProjectService;

    /**
     * 新增项目
     */
    @PostMapping("insert")
    @ApiOperation(value = "插入")
    public ResponseBean insert(@RequestBody ProjectDto projectDto) {
        return creationProjectService.insert(projectDto);
    }

    /**
     * preUpdate
     */
    @GetMapping("pre-update")
    @ApiOperation(value = "预修改")
    public ResponseBean preUpdate(@RequestParam Integer projectId) {
        return creationProjectService.preUpdate(projectId);
    }

    /**
     * update
     */
    @PostMapping("update")
    @ApiOperation(value = "更新")
    public ResponseBean update(@RequestBody ProjectDto projectDto) {
        return creationProjectService.update(projectDto);
    }

    /**
     * list
     */
    @PostMapping("list")
    @ApiOperation(value = "列表")
    public ResponseBean list(@RequestBody PageParamDto pageParamDto) {
        return creationProjectService.list(pageParamDto);
    }

    /**
     * 超级管理员进行审核
     */
    @PostMapping("audit")
    @ApiOperation(value = "超级管理员审核")
    public ResponseBean audit(@RequestBody AuditDto auditDto) {
        return creationProjectService.audit(auditDto);
    }

}
