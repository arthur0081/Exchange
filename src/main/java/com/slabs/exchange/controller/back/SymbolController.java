package com.slabs.exchange.controller.back;

import com.slabs.exchange.model.common.ResponseBean;
import com.slabs.exchange.model.dto.SymbolDto;
import com.slabs.exchange.service.back.ISymbolService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 币对
 */
@RestController
@RequestMapping("symbol")
public class SymbolController {

    @Resource
    private ISymbolService symbolService;

    /**
     * 新增币对
     */
    @PostMapping("insert")
    public ResponseBean insert(@RequestBody SymbolDto symbolDto) {
        return symbolService.insert(symbolDto);
    }

    /**
     * 预修改
     */
    @PostMapping("pre-update")
    public ResponseBean preUpdate(@RequestBody Integer symbolId) {
        return symbolService.preUpdate(symbolId);
    }

    /**
     * 修改
     */
    @PostMapping("update")
    public ResponseBean update(@RequestBody SymbolDto symbolDto) {
        return symbolService.update(symbolDto);
    }

}
