package com.slabs.exchange.model.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ProjectCoinDto {
    private Long id;

    private String name;

    private String verboseName;

    private Long precision;

    private BigDecimal amount;

    /**
     * 附件
     */
    private List<AttachFileDto> attachFileList;

    private static final long serialVersionUID = 1L;
}
