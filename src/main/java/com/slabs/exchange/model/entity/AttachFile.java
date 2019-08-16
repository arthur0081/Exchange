package com.slabs.exchange.model.entity;

import io.swagger.models.auth.In;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * attach_file
 * @author 
 */
@Data
public class AttachFile implements Serializable {
    private Long id;

    private String type;

    private String fileName;

    private String filePath;

    private Integer refId;

    private String isDel;

    private Long createUser;

    private Date createTime;

    private Integer modifyUser;

    private Date modifyTime;

    private static final long serialVersionUID = 1L;
}