package com.slabs.exchange.service.impl;

import com.slabs.exchange.common.exception.ExchangeException;
import com.slabs.exchange.common.exception.RespBizMessageEnum;
import com.slabs.exchange.service.IFileService;
import com.slabs.exchange.util.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

@Slf4j
@Service
public class FileServiceImpl implements IFileService {

    /**
     * 把上传的文件保存在本地磁盘上
     */
    @Override
    public String saveFile(MultipartFile multipartFile, HttpServletRequest request) {
        String fileName = multipartFile.getOriginalFilename();
        String ext = fileName.substring(fileName.lastIndexOf(".") + 1);

        //上传文件
        try {
            // 构建唯一url  这里模拟就随机生成一个即可
            String filePath = "f://exchange/pictures/" + System.currentTimeMillis() + "_" + fileName;
            // 存储到本地
            FileUtil.writeFile(multipartFile.getInputStream(), filePath);
            multipartFile.getInputStream().close();
            return filePath;

        } catch (Exception e) {
            log.error("上传失败", e);
            throw new ExchangeException(RespBizMessageEnum.FILE_ERROR);
        }
    }

    /**
     * 根据唯一 filePath 进行下载
     */
    @Override
    public void download(HttpServletResponse response, String filePath, String fileName) {
        OutputStream out = null;
        InputStream is = null;
        response.reset();
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/msword");
        response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
        File file = new File(filePath);
        if (file != null && file.exists() && file.isFile()) {
            try {
                out = new BufferedOutputStream(response.getOutputStream());
                is = new BufferedInputStream(new FileInputStream(new File(filePath)));
                byte[] content = new byte[1024];
                int len = 0;
                while((len = is.read(content)) > 0) {
                    out.write(content, 0, len);
                }
                out.flush();
            } catch (IOException e1) {
                e1.printStackTrace();
            } finally {
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
