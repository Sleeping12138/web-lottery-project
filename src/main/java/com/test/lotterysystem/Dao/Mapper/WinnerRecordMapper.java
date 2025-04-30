package com.test.lotterysystem.Dao.Mapper;

import com.test.lotterysystem.Dao.DaoObject.WinnerDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface WinnerRecordMapper {

    void saveWinnerInfo(List<WinnerDO> winnerDOS);

    List<WinnerDO> selectByActivityId(Long activityId);


    int countByActivityIdAndPrizeId(Long activityId, Long prizeId);

    void rollBack(Long activityId, Long prizeId);

    List<WinnerDO> getWinnerInfoByAPId(Long activityId, Long prizeId);
}
