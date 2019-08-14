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
     * 保本区 1（默认），创意区 2
     */
    private Integer areaType = 1;

    /**
     * 债务人id
     */
    private Integer debitor;

    private static final long serialVersionUID = 1L;
}
