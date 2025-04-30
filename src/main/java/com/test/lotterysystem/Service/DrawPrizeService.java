package com.test.lotterysystem.Service;


import com.test.lotterysystem.Controller.Param.DrawPrizeParam;
import com.test.lotterysystem.Controller.Param.WinnerRecordParam;
import com.test.lotterysystem.Dao.DaoObject.WinnerDO;
import com.test.lotterysystem.Service.DTO.WinnerRecordInfoDTO;

import java.util.List;


public interface DrawPrizeService {
    void saveDrawPrizeInfo(DrawPrizeParam drawPrizeParam);

    boolean checkDrawPrizeMessage(DrawPrizeParam drawPrizeParam);

    List<WinnerDO> saveWinnerInfo(DrawPrizeParam message);

    void rollBackWinner(Long activityId, Long prizeId);

    List<WinnerRecordInfoDTO> getWinnerRecord(WinnerRecordParam winnerRecordParam);

    List<WinnerDO> getWinnerInfoList(String key);
}
