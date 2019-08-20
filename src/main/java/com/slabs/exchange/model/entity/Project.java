package com.slabs.exchange.model.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * project
 * @author 
 */
@Data
public class Project implements Serializable {
    private Long id;

    private String name;

    private String addr;

    private BigDecimal investAmount;

    private BigDecimal collectAmount;

    private String investTender;

    private String projectType;

    private String fundUse;

    private Integer investPeriod;

    private Date startTime;

    private BigDecimal expectYearBonus;

    private String bonusSharePeriod;

    private Date bonusComputeTime;

    private String receiptWay;

    private Date endTime;

    private String paybackWay;

    private Date paybackTime;

    private String paybackTimePeriod;

    private String tenderAdmin;

    private BigDecimal adminFee;

    private String holdCost;

    private BigDecimal bonusFee;

    private String bonusResource;

    private String guarantee;

    private String briefIntroduction;

    private String perimeterMatch;

    private String marketAnalysis;

    private String cityCondition;

    private String costAnalysis;

    private String bonusAnalysis;

    private String riskAnalysis;

    private String adminIntroduction;

    private String projectPlatformAccount;

    private Long symbol;

    private String status;

    private Long createUser;

    private Date createTime;

    private Object modifyUser;

    private Date modifyTime;

    private String other;

    private Integer areaType;

    private BigDecimal initPrice;

    private static final long serialVersionUID = 1L;
}