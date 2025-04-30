package com.test.lotterysystem.Controller;

import com.test.lotterysystem.Comment.Utils.MyBeanUtil;
import com.test.lotterysystem.Controller.Param.DrawPrizeParam;
import com.test.lotterysystem.Controller.Param.WinnerRecordParam;
import com.test.lotterysystem.Controller.Result.WinnerRecordInfo;
import com.test.lotterysystem.Service.DTO.WinnerRecordInfoDTO;
import com.test.lotterysystem.Service.DrawPrizeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/draw")
@RestController
@Slf4j
public class DrawPrizeController {
    @Autowired
    private DrawPrizeService drawPrizeService;

    /**
     * 保存活动的某个奖品的中奖信息
     * @param drawPrizeParam
     * @return
     */
    @RequestMapping("/prize")
    public Boolean drawPrize(@Validated @RequestBody DrawPrizeParam drawPrizeParam) {
        log.info("drawPrizeParam为：--------{}", drawPrizeParam);
        drawPrizeService.saveDrawPrizeInfo(drawPrizeParam);
        return true;
    }



    /**
     * 获取当前活动的所有获奖信息
     * @param winnerRecordParam
     * @return
     */
    @RequestMapping("/getWinnerRecord")
    public List<WinnerRecordInfo> getWinnerRecord(@Validated @RequestBody WinnerRecordParam winnerRecordParam){
        log.info("winnerRecordParam为：{}",winnerRecordParam);
        List<WinnerRecordInfoDTO> winnerRecordInfoDTOList = drawPrizeService.getWinnerRecord(winnerRecordParam);
        return MyBeanUtil.transOfWinnerRecordInfo(winnerRecordInfoDTOList);
    }
}
