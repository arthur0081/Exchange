package com.slabs.exchange.model.entity;

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

    private Long refId;

    private String isDel;

    private Long createUser;

    private Date createTime;

    private Long modifyUser;

    private Date modifyTime;

    private static final long serialVersionUID = 1L;
}