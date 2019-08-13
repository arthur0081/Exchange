package com.slabs.exchange.controller;

import com.slabs.exchange.service.IFileService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

/**
 * 模拟一个文件服务器  上传和下载功能
 */
@RestController
@RequestMapping("file")
public class FileController {
    @Resource
    private IFileService fileService;

    @PostMapping(value = "/upload",produces="text/html;charset=utf-8")
    public String upload(@RequestParam(value = "file") MultipartFile file) {
        System.out.println(file.getSize());
        String url = fileService.saveFile(file);
        return url;
    }

    @PostMapping(value = "/download")
    public void download(HttpServletResponse response, @RequestParam(value = "filePath") String filePath, @RequestParam(value = "fileName") String fileName) {
        fileService.download(response, filePath, fileName);
    }

}
