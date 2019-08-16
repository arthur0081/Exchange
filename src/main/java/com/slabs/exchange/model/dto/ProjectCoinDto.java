package com.slabs.exchange.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ProjectCoinDto {

    @ApiModelProperty(notes = "项目币id")
    private Long id;

    @ApiModelProperty(notes = "币名称")
    private String name;

    @ApiModelProperty(notes = "币全称")
    private String verboseName;

    @ApiModelProperty(notes = "精度")
    private Long precision;

    @ApiModelProperty(notes = "币数量")
    private BigDecimal amount;

    /**
     * 附件
     */
    @ApiModelProperty(notes = "附件")
    private List<AttachFileDto> attachFileList;

    private static final long serialVersionUID = 1L;
}
