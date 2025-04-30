package com.test.lotterysystem.Controller;

import com.test.lotterysystem.Service.PictureService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping("/pic")
@RestController
@Slf4j
public class PictureController {
    @Autowired
    private PictureService pictureService;

    /**
     * 保存图片到本地指定位置
     * @param picture
     * @return
     */
    @RequestMapping("save")
    public String savePicture(MultipartFile picture) {
        return pictureService.savePicture(picture);
    }
}
