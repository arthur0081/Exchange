package com.slabs.exchange.model.dto;

import com.slabs.exchange.model.entity.AttachFile;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
public class ProjectDto implements Serializable {
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

    private Integer coin;

    private Integer userId;

    /**
     * 附件
     */
    private Map<String, List<AttachFileDto>> attachFileMap;

    /**
     * 点击保存，还是点击提交
     */
    private String clickType;

    /**
     * 回显 所有的附件
     */
    private List<Map<String, List<AttachFile>>> attachList;

    private static final long serialVersionUID = 1L;
}
