package com.slabs.exchange.model.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * sysuser
 * @author 
 */
@Data
public class User implements Serializable {
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

    private String salt;

    private String walletAddr;

    private static final long serialVersionUID = 1L;
}