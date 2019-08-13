package com.slabs.exchange.controller.back;

import com.slabs.exchange.model.common.ResponseBean;
import com.slabs.exchange.model.dto.PageParamDto;
import com.slabs.exchange.model.dto.ProjectCoinDto;
import com.slabs.exchange.service.back.IProjectCoinService;
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
public class ProjectCoinController {

    @Resource
    private IProjectCoinService projectCoinService;

    /**
     * 新增项目币
     */
    @PostMapping("insert")
    public ResponseBean insert(@RequestBody ProjectCoinDto projectCoinDto) {
        return projectCoinService.insert(projectCoinDto);
    }

    /**
     * 预修改（准备修改时的回显）
     */
    @PostMapping("pre-update")
    public ResponseBean preUpdate(@RequestBody Long coinId) {
        return projectCoinService.preUpdate(coinId);
    }

    /**
     * 修改
     */
    @PostMapping("update")
    public ResponseBean update(@RequestBody ProjectCoinDto projectCoinDto) {
        return projectCoinService.update(projectCoinDto);
    }

    /**
     * 列表查询
     */
    @PostMapping("list")
    public ResponseBean list(@RequestBody PageParamDto pageParamDto) {
        return projectCoinService.list(pageParamDto);
    }

    /**
     *  得到所有的商品货币（本质就是项目币）
     */
    @PostMapping("getProjectCoins")
    public ResponseBean getProjectCoins() {
        return projectCoinService.getProjectCoins();
    }

    /**
     * 币的简称不能重复
     * TODO 创建的时候直接查询库(这个时候只有name字段)
     * TODO 修改的时候要排除自己(id和name字段)
     */
    @PostMapping("check")
    public ResponseBean checkName(@RequestBody ProjectCoinDto projectCoinDto) {
       // return projectCoinService.checkName(projectCoinDto);
        return null;
    }


}
