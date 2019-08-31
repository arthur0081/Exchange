package com.slabs.exchange.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author joey
 * @date 2018/2/11
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class OauthInfoDto implements IBaseDto {


    @ApiModelProperty(notes = "角色Id")
    private Integer roleId;
    @ApiModelProperty(notes = "用户ID")
    private Integer userId;
    @ApiModelProperty(notes = "账户")
    private String account;
    @ApiModelProperty(notes = "用户token")
    private String token;
    @ApiModelProperty(notes = "用户sessionId")
    private String sessionId;
    @ApiModelProperty(notes = "权限")
    private List<String> permissions;
    @ApiModelProperty(notes = "审核状态")
    private Integer auditState;

}
