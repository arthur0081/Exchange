package com.slabs.exchange.model.dto;


import lombok.Data;

import java.math.BigDecimal;
import java.util.List;


@Data
public class InvitationRecordDto {

    // 用户id
    private Integer userId;

    // 用户账户
    private String account;

    // 数量
    private BigDecimal amount;

    // 姓名
    private String userName;

    //邀请人的账户列表
    private List<String> accountList;
}
