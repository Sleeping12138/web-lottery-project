package com.test.lotterysystem.Dao.Mapper;

import com.test.lotterysystem.Dao.DaoObject.ActivityPrizeDO;
import com.test.lotterysystem.Service.Enum.ActivityPrizeEnum;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ActivityPrizeMapper {
    void batchInsertActivityPrize(List<ActivityPrizeDO> activityPrizeDOS);

    List<ActivityPrizeDO> getActivityPrizeDetail(Long activityId);

    ActivityPrizeDO getActivityPrize(Long activityId,Long prizeId);

    int countPrize(Long activityId, String status);

    void updateStatus(Long activityId, Long prizeId, String name);
}
