package com.slabs.exchange.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
public class UserDto {

    private Integer id;

    private String account;

    private String password;

    private String username;

    private Date regTime;

    private String email;

    private String fundPassword;

    private Integer createUser;

    private Integer modifyUser;

    private Date modifyTime;

    private List<Integer> roleList;

    private String invitationCode;

    private Integer certificateType;

    private String certificateNum;

    private String nationality;

    /**
     * 审核状态记录
     */
    private Integer auditState;

    /**
     * 护照
     */
    @ApiModelProperty(notes = "护照")
    private List<AttachFileDto> attachFileList;

    /**
     * 身份证
     */
    @ApiModelProperty(notes = "身份证")
    private Map<String, List<AttachFileDto>> attachFileDtoMap;

    /**
     * 前台登陆或者后台登陆
     */
    private String loginType;

    private static final long serialVersionUID = 1L;
}
