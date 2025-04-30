package com.test.lotterysystem.Service.Impl;

import com.test.lotterysystem.Service.PictureService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
public class PictureServiceImpl implements PictureService {

    @Value("${pic.local-path}")
    private String path;

    @Override
    public String savePicture(MultipartFile multipartFile) {
        // 创建一个路径对象
        File file = new File(path);

        if (!file.exists()) {
            file.mkdirs();
        }

        String fileName = multipartFile.getOriginalFilename();
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        String ret = UUID.randomUUID().toString().replace("-", "") + suffix;
        try {
            multipartFile.transferTo(new File(path + "/" + ret));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return ret;
    }
}
