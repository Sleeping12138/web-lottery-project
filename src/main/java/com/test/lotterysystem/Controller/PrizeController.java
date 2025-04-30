package com.test.lotterysystem.Controller;

import com.test.lotterysystem.Comment.Utils.MyBeanUtil;
import com.test.lotterysystem.Controller.Param.CreatePrizeParam;
import com.test.lotterysystem.Controller.Param.PageParam;
import com.test.lotterysystem.Controller.Result.PrizeInfo;
import com.test.lotterysystem.Controller.Result.PrizeListResult;
import com.test.lotterysystem.Service.DTO.PrizeDTO;
import com.test.lotterysystem.Service.DTO.PrizeListResultDTO;
import com.test.lotterysystem.Service.PrizeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.stream.Collectors;

@RequestMapping("/prize")
@RestController
@Slf4j
public class PrizeController {

    @Autowired
    private PrizeService prizeService;

    /**
     * 创建商品
     * @param createPrizeParam
     * @param multipartFile
     * @return 返回商品的id
     */
    @RequestMapping("/create")
    public Long create(@Validated @RequestPart("param") CreatePrizeParam createPrizeParam
            , @RequestPart("prizePic") MultipartFile multipartFile){
        log.info("createPrizeParam为：----{}",createPrizeParam);
        return prizeService.create(createPrizeParam,multipartFile);
    }

    /**
     * 获取商品列表
     * @param pageParam
     * @return
     */
    @RequestMapping("/getPrizeList")
    public PrizeListResult getPrizeList(PageParam pageParam){
        log.info("pageParam为：--------{}",pageParam);
        PrizeListResultDTO<PrizeDTO> prizeListResultDTO =  prizeService.getPrizeList(pageParam);
        return MyBeanUtil.transOfPrizeListResultDTO(prizeListResultDTO);
    }

}
