package com.test.lotterysystem.Service.ActivityStatusChange.Operator;

import com.test.lotterysystem.Dao.DaoObject.ActivityPrizeDO;
import com.test.lotterysystem.Dao.Mapper.ActivityPrizeMapper;
import com.test.lotterysystem.Service.DTO.ConvertActivityStatusDTO;
import com.test.lotterysystem.Service.Enum.ActivityPrizeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PrizeOperator extends AbstractOperator {

    @Autowired
    private ActivityPrizeMapper activityPrizeMapper;

    @Override
    public Integer sequence() {
        return 1;
    }

    @Override
    public Boolean needConvert(ConvertActivityStatusDTO convertActivityStatusDTO) {
        Long activityId = convertActivityStatusDTO.getActivityId();
        Long prizeId = convertActivityStatusDTO.getPrizeId();
        ActivityPrizeEnum targetPrizeStatus = convertActivityStatusDTO.getTargetPrizeStatus();
        if (activityId == null || prizeId == null || targetPrizeStatus == null) {
            return false;
        }

        ActivityPrizeDO activityPrize = activityPrizeMapper.getActivityPrize(activityId, prizeId);
        if (activityPrize == null) {
            return false;
        }

        if (activityPrize.getStatus().equalsIgnoreCase(targetPrizeStatus.name())) {
            return false;
        }

        return true;
    }

    @Override
    public Boolean convert(ConvertActivityStatusDTO convertActivityStatusDTO) {
        try {
            activityPrizeMapper.updateStatus(convertActivityStatusDTO.getActivityId()
                    , convertActivityStatusDTO.getPrizeId(), convertActivityStatusDTO.getTargetPrizeStatus().name());
            return true;
        } catch (Exception e) {
            log.error("奖品状态转换异常");
            return false;
        }
    }
}
