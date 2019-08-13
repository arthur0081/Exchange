package com.slabs.exchange.model.dto;

import lombok.Data;

@Data
public class AttachFileDto {
    private Long id;

    private String type;

    private String fileName;

    private String filePath;

    private Long refId;

    private static final long serialVersionUID = 1L;
}
