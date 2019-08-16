package com.slabs.exchange.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

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
     * 附件
     */
    @ApiModelProperty(notes = "附件")
    private List<AttachFileDto> attachFileList;

    private static final long serialVersionUID = 1L;
}
