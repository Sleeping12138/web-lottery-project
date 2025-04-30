package com.test.lotterysystem.Service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


public interface PictureService {

    String savePicture(MultipartFile multipartFile);
}
