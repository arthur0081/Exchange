package com.slabs.exchange.model.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProjectListDto {
    // 项目id
    private Long id;
    // 币对名称
    private String symbolName;
    // 项目名称
    private String proName;
    // 币总数
    private BigDecimal amount;
    // 项目状态
    private String status;
}
