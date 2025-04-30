package com.test.lotterysystem.Service.Impl;

import com.test.lotterysystem.Comment.Utils.MyBeanUtil;
import com.test.lotterysystem.Controller.Param.CreatePrizeParam;
import com.test.lotterysystem.Controller.Param.PageParam;
import com.test.lotterysystem.Dao.DaoObject.PrizeDO;
import com.test.lotterysystem.Dao.DaoObject.priceDO;
import com.test.lotterysystem.Dao.Mapper.PrizeMapper;
import com.test.lotterysystem.Service.DTO.PrizeDTO;
import com.test.lotterysystem.Service.DTO.PrizeListResultDTO;
import com.test.lotterysystem.Service.PictureService;
import com.test.lotterysystem.Service.PrizeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PrizeServiceImpl implements PrizeService {

    @Autowired
    private PictureService pictureService;

    @Autowired
    private PrizeMapper prizeMapper;

    /**
     * 创建商品
     * @param createPrizeParam
     * @param multipartFile
     * @return
     */
    @Override
    public Long create(CreatePrizeParam createPrizeParam, MultipartFile multipartFile) {
        // 将文件写道静态文件目录
        String imageUrl = pictureService.savePicture(multipartFile);

        // 将数据存储到数据库
        priceDO priceDO = new priceDO();
        priceDO.setPrizeName(createPrizeParam.getPrizeName());
        priceDO.setDescription(createPrizeParam.getDescription());
        priceDO.setImageUrl(imageUrl);
        priceDO.setPrice(createPrizeParam.getPrice());

        prizeMapper.create(priceDO);

        return priceDO.getId();
    }

    /**
     * 获取商品列表
     *
     * @param pageParam
     * @return
     */
    @Override
    public PrizeListResultDTO<PrizeDTO> getPrizeList(PageParam pageParam) {
        PrizeListResultDTO<PrizeDTO> ret = new PrizeListResultDTO<PrizeDTO>();
        // 获取总商品数
        Integer count = prizeMapper.getCount();
        ret.setTotal(count);

        // 获取商品列表
        List<PrizeDO> list = prizeMapper.getPrizeList(
                pageParam.offset(pageParam.getCurrentPage(),pageParam.getPageSize()),pageParam.getPageSize());
        ret.setRecords(list.stream().map(prizeDO -> {
            PrizeDTO prizeDTO = MyBeanUtil.transOfPrizeDTO(prizeDO);
            return prizeDTO;
        }).collect(Collectors.toList()));

        log.info("商品列表信息: {}",ret);
        return ret;
    }

}
