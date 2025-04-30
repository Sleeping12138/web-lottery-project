package com.test.lotterysystem.Service;

import com.test.lotterysystem.Controller.Param.CreatePrizeParam;
import com.test.lotterysystem.Controller.Param.PageParam;
import com.test.lotterysystem.Service.DTO.PrizeDTO;
import com.test.lotterysystem.Service.DTO.PrizeListResultDTO;
import org.springframework.web.multipart.MultipartFile;

public interface PrizeService {

    Long create(CreatePrizeParam createPrizeParam, MultipartFile multipartFile);

    PrizeListResultDTO<PrizeDTO> getPrizeList(PageParam pageParam);
}
