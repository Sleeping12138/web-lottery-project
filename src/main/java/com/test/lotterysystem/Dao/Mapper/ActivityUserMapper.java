package com.test.lotterysystem.Dao.Mapper;

import com.test.lotterysystem.Dao.DaoObject.ActivityUserDO;
import com.test.lotterysystem.Service.Enum.ActivityEnum;
import com.test.lotterysystem.Service.Enum.ActivityPrizeEnum;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ActivityUserMapper {
    void batchInsertActivityUser(List<ActivityUserDO> userDOS);

    List<ActivityUserDO> getActivityUserDetail(Long activityId);

    List<ActivityUserDO> batchSelectByAUIds(Long activityId, List<Long> userIds, ActivityEnum status);

    void updateStatus(Long activityId, List<Long> userIds, String status);
}
