package com.test.lotterysystem.Service.ActivityStatusChange.Operator;

import com.test.lotterysystem.Dao.DaoObject.ActivityUserDO;
import com.test.lotterysystem.Dao.Mapper.ActivityUserMapper;
import com.test.lotterysystem.Service.DTO.ConvertActivityStatusDTO;
import com.test.lotterysystem.Service.Enum.ActivityEnum;
import com.test.lotterysystem.Service.Enum.ActivityPrizeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Component
@Slf4j
public class UserOperator extends AbstractOperator {
    @Autowired
    private ActivityUserMapper activityUserMapper;

    @Override
    public Integer sequence() {
        return 1;
    }

    @Override
    public Boolean needConvert(ConvertActivityStatusDTO convertActivityStatusDTO) {
        Long activityId = convertActivityStatusDTO.getActivityId();
        List<Long> userIds = convertActivityStatusDTO.getUserIds();
        ActivityEnum status = convertActivityStatusDTO.getTargetActivityStatus();
        if (activityId == null || CollectionUtils.isEmpty(userIds) || status == null) {
            return false;
        }

        List<ActivityUserDO> activityUserDOS = activityUserMapper.batchSelectByAUIds(activityId, userIds, status);
        if (CollectionUtils.isEmpty(activityUserDOS)) {
            return false;
        }

        for (ActivityUserDO user : activityUserDOS) {
            if (user.getStatus().name().equalsIgnoreCase(status.name())) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Boolean convert(ConvertActivityStatusDTO convertActivityStatusDTO) {
        try {
            Long activityId = convertActivityStatusDTO.getActivityId();
            List<Long> userIds = convertActivityStatusDTO.getUserIds();
            ActivityPrizeEnum targetPrizeStatus = convertActivityStatusDTO.getTargetPrizeStatus();
            activityUserMapper.updateStatus(activityId, userIds, targetPrizeStatus.name());
            return true;
        } catch (Exception e) {
            log.error("用户状态改变发生异常");
            return false;
        }
    }
}
