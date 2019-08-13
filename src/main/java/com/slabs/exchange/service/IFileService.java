package com.slabs.exchange.service;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

public interface IFileService {

    String saveFile(MultipartFile file);

    void download(HttpServletResponse response, String filePath, String fileName);
}
