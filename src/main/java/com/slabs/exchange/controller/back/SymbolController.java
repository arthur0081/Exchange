package com.slabs.exchange.controller.back;

import com.slabs.exchange.model.common.ResponseBean;
import com.slabs.exchange.model.dto.PageParamDto;
import com.slabs.exchange.model.dto.SymbolDto;
import com.slabs.exchange.service.back.ISymbolService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 币对
 */
@RestController
@RequestMapping("symbol")
@Api(value = "币对", description = "币对模块：币对相关接口")
public class SymbolController {

    @Resource
    private ISymbolService symbolService;

    /**
     * 新增币对
     */
    @ApiOperation(value = "插入")
    @PostMapping("insert")
    public ResponseBean insert(@RequestBody SymbolDto symbolDto) {
        return symbolService.insert(symbolDto);
    }

    /**
     * 预修改
     */
    @ApiOperation(value = "预修改")
    @PostMapping("pre-update")
    public ResponseBean preUpdate(@RequestBody int symbolId) {
        return symbolService.preUpdate(symbolId);
    }

    /**
     * 修改
     */
    @ApiOperation(value = "修改")
    @PostMapping("update")
    public ResponseBean update(@RequestBody SymbolDto symbolDto) {
        return symbolService.update(symbolDto);
    }

    /**
     * 币对列表
     */
    @ApiOperation(value = "币对列表")
    @PostMapping("list")
    public ResponseBean list(@RequestBody PageParamDto pageParamDto) {
        return symbolService.list(pageParamDto);
    }

    /**
     * 所有有效的币对
     */
    @ApiOperation(value = "所有有效的币对")
    @PostMapping("valid-symbol")
    public ResponseBean getAllValidSymbols() {
        return symbolService.getAllValidSymbols();
    }

    @ApiOperation(value = "所有的币对")
    @PostMapping("all-symbol")
    public ResponseBean getAllSymbols() {
        return symbolService.getAllSymbols();
    }

    /**
     * 获取所有有效的稳定区币对(如果有参数则获取一个)
     */
    @ApiOperation(value = "获取所有有效的稳定区币对(如果有参数则获取一个)")
    @GetMapping("valid-stable-symbol")
    public ResponseBean getAllStableSymbols(@RequestParam(value = "symbolId", required = false) Integer symbolId) {
        return symbolService.getAllStableSymbols(symbolId);
    }

    /**
     * 获取所有有效的创新区币对（如果有参数则获取一个）
     */
    @ApiOperation(value = "获取所有有效的创新区币对（如果有参数则获取一个）")
    @GetMapping("valid-creation-symbol")
    public ResponseBean getAllCreationSymbols(@RequestParam(value = "symbolId", required = false) Integer symbolId) {
        return symbolService.getAllCreationSymbols(symbolId);
    }

}
