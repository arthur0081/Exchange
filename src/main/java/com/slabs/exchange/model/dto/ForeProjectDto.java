package com.slabs.exchange.model.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class ForeProjectDto {
    private Long id;

    private String name;

    private BigDecimal collectAmount;

    private Integer investPeriod;

    private Date startTime;

    private BigDecimal expectYearBonus;

    private Date endTime;

    private Long symbol;

    private String status;

    /**
     *币对名称
     */
    private String symbolName;

    /**
     * 已经认购的额度
     */
    private BigDecimal boughtAmount;

    private static final long serialVersionUID = 1L;
}
