//package com.slabs.exchange.controller.fore;
//
//
//import com.slabs.exchange.model.common.ResponseBean;
//import com.slabs.exchange.model.dto.BuyDto;
//import com.slabs.exchange.model.dto.PageParamDto;
//import com.slabs.exchange.service.back.ICreationProjectService;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import javax.annotation.Resource;
//
///**
// * 前台 项目列表
// */
//@RestController
//@RequestMapping("fore-creation-project")
//@Api(value = "前台创新项目列表", description = "前台创新项目列表模块：前台创新项目相关接口")
//public class ForeCreationProjectController {
//
//    @Resource
//    private ICreationProjectService creationProjectService;
//
//    /**
//     * 前台列表查询（稳定区）
//     */
//    @PostMapping("fore-list")
//    @ApiOperation(value = "前台列表查询（创新）")
//    public ResponseBean getForeProjectList(@RequestBody PageParamDto pageParamDto) {
//        return creationProjectService.getForeProjectList(pageParamDto);
//    }
//
//    /**
//     * 前台： 详情
//     */
//    @PostMapping("detail")
//    @ApiOperation(value = "前台： 创新项目详情")
//    public ResponseBean getForeProjectDetail(@RequestBody Long projectId) {
//        return creationProjectService.getForeProjectDetail(projectId);
//    }
//
//    /**
//     * 前台： 买入
//     */
//    @PostMapping("buy")
//    @ApiOperation(value = "前台： 创新项目买入")
//    public ResponseBean buy(@RequestBody BuyDto buyDto) {
//        return creationProjectService.buy(buyDto);
//    }
//}
