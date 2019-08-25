package com.slabs.exchange.model.dto;

import com.slabs.exchange.model.entity.AttachFile;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

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

    private BigDecimal initPrice;

    /**
     *币对名称
     */
    private String symbolName;

    /**
     * 项目简介
     */
    private String briefIntroduction;

    /**
     * 项目类别
     */
    private String projectType;

    private List<AttachFile> fileList;

    /**
     * 已经认购的额度
     */
    private BigDecimal boughtAmount;

    private static final long serialVersionUID = 1L;
}
