package com.slabs.exchange.model.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class PageParamDto implements Serializable {
    /**
     * 当前页数
     */
    private int currentPage = 1;

    /**
     * 每页多少条
     */
    private int pageSize = 10;

    /**
     * 总记录数
     */
    private int count;

    /**
     * 开始条数
     */
    private int start;

    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 项目状态
     */
    private String projectStatus;

    /**
     * 稳定区 1（默认），创意区 2
     */
    private Integer areaType = 1;

    /**
     * 债务人id
     */
    private Integer debitor;

    /**
     * 债务人id
     */
    private Integer creditor;

    /**
     * 用户账户
     */
    private String account;

    /**
     * 币对Id
     */
    private Integer symbol;

    /**
     * 买或者卖
     */
    private String side;

    /**
     * 币种
     */
    private String coin;

    /**
     * 24小时换手率
     */
    private Integer hourChange = 1;

    private Integer userId;

    private Integer projectId;

    /**
     * 审核状态
     */
    private Integer auditState;

    private static final long serialVersionUID = 1L;
}
