package com.test.lotterysystem.Service.ActivityStatusChange.Impl;

import com.test.lotterysystem.Comment.ErrorCode.ServiceErrorCode;
import com.test.lotterysystem.Comment.Exception.ServiceException;
import com.test.lotterysystem.Controller.Param.DrawPrizeParam;
import com.test.lotterysystem.Service.ActivityService;
import com.test.lotterysystem.Service.ActivityStatusChange.ActivityStatusChange;
import com.test.lotterysystem.Service.ActivityStatusChange.Operator.AbstractOperator;
import com.test.lotterysystem.Service.DTO.ConvertActivityStatusDTO;
import com.test.lotterysystem.Service.Enum.ActivityEnum;
import com.test.lotterysystem.Service.Enum.ActivityPrizeEnum;
import com.test.lotterysystem.Service.Enum.ActivityUserEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ActivityStatusChangeImpl implements ActivityStatusChange {

    @Autowired
    private Map<String, AbstractOperator> map;

    @Autowired
    private ActivityService activityService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void changeStatus(DrawPrizeParam message) {
        if (message==null) {
            log.error("接收到的数据为空");
            return;
        }

        if(map.isEmpty()){
            log.error("map为空");
            return;
        }

        // 转换内容的ID和要转换的状态
        ConvertActivityStatusDTO statusDTO = new ConvertActivityStatusDTO();
        statusDTO.setActivityId(message.getActivityId());
        statusDTO.setTargetActivityStatus(ActivityEnum.FINISH);
        statusDTO.setPrizeId(message.getPrizeId());
        statusDTO.setTargetPrizeStatus(ActivityPrizeEnum.COMPLETED);
        statusDTO.setUserIds(message.getWinnerList().stream().map(user->user.getUserId()).collect(Collectors.toList()));
        statusDTO.setTargetUserStatus(ActivityUserEnum.COMPLETED);


        Boolean flag = false;
        Map<String, AbstractOperator> curMap = new HashMap<>(map);

        // 先改变优先级为1的
        flag = processConvertStatus(statusDTO, 1, curMap);

        // 后改变优先级为2的
        flag = processConvertStatus(statusDTO, 2, curMap) || flag;

        // 对修改后的数据进行缓存到redis中
        if (flag) {
            activityService.cacheActivity(statusDTO.getActivityId());
        }
    }

    private Boolean processConvertStatus(ConvertActivityStatusDTO convertActivityStatusDTO, int score
            , Map<String, AbstractOperator> curMap) {

        boolean flag = false;
        Iterator<Map.Entry<String, AbstractOperator>> iterator = curMap.entrySet().iterator();
        while (iterator.hasNext()) {
            AbstractOperator value = iterator.next().getValue();
            if (value.sequence() != score || !value.needConvert(convertActivityStatusDTO)) {
                continue;
            }

            if (!value.convert(convertActivityStatusDTO)) {
                log.error("修改数据失败...");
                throw new ServiceException(ServiceErrorCode.CONVERT_ERROR);
            }

            iterator.remove();
            flag = true;
        }
        return flag;
    }

    /**
     * 事件进行回滚操作
     *
     * @param convertActivityStatusDTO
     */
    @Override
    public void rollbackStatus(ConvertActivityStatusDTO convertActivityStatusDTO) {
        for (Map.Entry<String, AbstractOperator> entry : map.entrySet()) {
            entry.getValue().convert(convertActivityStatusDTO);
        }
    }
}
