package com.slabs.exchange.model.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * invitation_record
 * @author 
 */
@Data
public class InvitationRecord implements Serializable {

    private Integer id;

    private Integer benefitUser;

    private Integer inviteeUser;

    private String coin;

    private BigDecimal amount;

    private Date time;

    private static final long serialVersionUID = 1L;
}