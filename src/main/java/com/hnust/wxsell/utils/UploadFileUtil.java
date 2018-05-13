package com.hnust.wxsell.utils;

import com.hnust.wxsell.config.ProjectUrlConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

/**
 * Create by HJT
 * 2018/3/19 11:16
 **/
@Slf4j
public class UploadFileUtil {

    public static String uploadProductImg(MultipartFile file, String filePath, String productName) throws Exception {

        String fileName = file.getOriginalFilename();

        String fileExtensionName = fileName.substring(fileName.lastIndexOf(".") + 1);

        String uploadFileName = UUID.randomUUID().toString() + "_" + productName + "." + fileExtensionName;

        log.info("【上传商品图片】 上传文件名:{}, 路径: {}, 新文件名:{}", fileName, filePath, uploadFileName);

        File targetFile = new File(filePath);
        if(!targetFile.exists()){
            targetFile.setWritable(true);
            targetFile.mkdirs();
        }

        FileOutputStream out = new FileOutputStream(filePath + uploadFileName);
        out.write(file.getBytes());
        out.flush();
        out.close();

        return "/sell/imgupload/" + uploadFileName;
    }
}
