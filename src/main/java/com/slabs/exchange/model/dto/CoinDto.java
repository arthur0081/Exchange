package com.slabs.exchange.model.dto;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CoinDto {
    private Integer id;

    private String name;

    private String verboseName;

    private Integer precision;

    /**
     * 该项目币的控制人
     */
    private Integer userId;

    /**
     * 该币的总量
     */
    private BigDecimal amount;

    /**
     * 账户
     */
    private String account;

    /**
     * 附件
     */
    @ApiModelProperty(notes = "附件")
    private List<AttachFileDto> attachFileList;
}
