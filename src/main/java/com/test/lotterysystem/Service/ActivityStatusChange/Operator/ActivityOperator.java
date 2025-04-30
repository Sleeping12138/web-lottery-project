package com.test.lotterysystem.Service.ActivityStatusChange.Operator;

import com.test.lotterysystem.Dao.DaoObject.ActivityDO;
import com.test.lotterysystem.Dao.Mapper.ActivityMapper;
import com.test.lotterysystem.Dao.Mapper.ActivityPrizeMapper;
import com.test.lotterysystem.Service.DTO.ConvertActivityStatusDTO;
import com.test.lotterysystem.Service.Enum.ActivityEnum;
import com.test.lotterysystem.Service.Enum.ActivityPrizeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ActivityOperator extends AbstractOperator {
    @Autowired
    private ActivityMapper activityMapper;

    @Autowired
    private ActivityPrizeMapper activityPrizeMapper;

    @Override
    public Integer sequence() {
        return 2;
    }

    @Override
    public Boolean needConvert(ConvertActivityStatusDTO convertActivityStatusDTO) {
        Long activityId = convertActivityStatusDTO.getActivityId();
        ActivityEnum status = convertActivityStatusDTO.getTargetActivityStatus();
        if (activityId == null || status == null) {
            return false;
        }

        ActivityDO activityDO = activityMapper.getActivityDetail(activityId);
        if (activityDO == null) {
            return false;
        }

        if (convertActivityStatusDTO.getTargetActivityStatus().name().equalsIgnoreCase(activityDO.getStatus())) {
            return false;
        }

        int number = activityPrizeMapper.countPrize(activityId, ActivityPrizeEnum.INIT.name());
        if (number > 0) {
            return false;
        }
        return true;
    }

    @Override
    public Boolean convert(ConvertActivityStatusDTO convertActivityStatusDTO) {
        try {
            activityMapper.updateStatus(convertActivityStatusDTO.getActivityId()
                    , convertActivityStatusDTO.getTargetActivityStatus().name());
            return true;
        } catch (Exception e) {
            log.error("活动转换时发生异常");
            return false;
        }
    }
}
