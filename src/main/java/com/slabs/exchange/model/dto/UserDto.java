package com.slabs.exchange.model.dto;

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

    private static final long serialVersionUID = 1L;
}
