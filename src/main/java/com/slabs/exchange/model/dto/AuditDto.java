package com.slabs.exchange.model.dto;

import lombok.Data;

@Data
public class AuditDto {
    /**
     * 超级管理员点击拒绝按钮
     */
    private String reject;

    /**
     * 项目id
     */
    private Long projectId;
}
